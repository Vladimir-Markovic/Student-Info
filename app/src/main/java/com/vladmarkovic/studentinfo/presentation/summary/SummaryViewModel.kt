package com.vladmarkovic.studentinfo.presentation.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vladmarkovic.studentinfo.application.util.subscribeIoObserveMain
import com.vladmarkovic.studentinfo.application.injection.annotation.PerActivity
import com.vladmarkovic.studentinfo.domain.exception.StudentInfoException
import com.vladmarkovic.studentinfo.domain.summary.SummaryRefresher
import com.vladmarkovic.studentinfo.domain.summary.model.CarPark
import com.vladmarkovic.studentinfo.domain.summary.model.ClassSession
import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBus
import com.vladmarkovic.studentinfo.presentation.common.BaseViewModel
import com.vladmarkovic.studentinfo.presentation.common.screenaction.Display
import com.vladmarkovic.studentinfo.presentation.summary.SummaryViewModel.DisplayAction.ShowError
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.*
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.ViewType.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function3
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
@PerActivity
class SummaryViewModel @Inject constructor(
        private val summaryRefresher: SummaryRefresher,
        classSessionsObservable: Observable<List<ClassSession>>,
        carParksObservable: Observable<List<CarPark>>,
        shuttleBusesObservable: Observable<List<ShuttleBus>>,
        exceptionObservable: Observable<StudentInfoException>) : BaseViewModel() {

    private companion object {
        // Assumed semester start date.
        private val startDate = Calendar.getInstance().apply { set(2019, 8, 1) }
    }

    private val studentName = MutableLiveData<String>()

    private val summaryData = MutableLiveData<List<SummaryItem>>()

    private val currentWeek = MutableLiveData<String>()

    // Used to be able to go back to 3 items after reducing to one.
    private var backupClassSessions = listOf<ClassSession>()
    private var backupCarParks = listOf<CarPark>()
    private var backupShuttleBuses = listOf<ShuttleBus>()

    /*
     * It observes class sessions, car parks and shuttle buses as separate observables,
     * combining latest to produce the complete view data,
     * manually adding hard coded title rows at start.
     */
    private val dataSubscription: Disposable =
            Observable.combineLatest(classSessionsObservable, carParksObservable, shuttleBusesObservable,
                    Function3<List<ClassSession>, List<CarPark>, List<ShuttleBus>, List<SummaryItem>>
                    { classSessions, carParks, shuttleBuses ->
                        backupClassSessions = classSessions
                        backupCarParks = carParks
                        backupShuttleBuses = shuttleBuses

                        createSummaryViewItems(classSessions, carParks, shuttleBuses)
                    })
                    .subscribeIoObserveMain()
                    .subscribe {
                        summaryData.value = it
                    }

    private val exceptionSubscription: Disposable =
            exceptionObservable
                    .subscribeIoObserveMain()
                    .subscribe {
                        display(ShowError(it))
                    }

    init {
        // In real situation we would get the name of the user after authenticating,
        // but here I'm simply providing one name only with no option for multiple (changing) users.
        studentName.value = "Jane"

        val now = Date()
        val formattedDate = SimpleDateFormat("dd/MM EEEE", Locale.getDefault()).format(now)

        val days = MILLISECONDS.toDays(now.time) - MILLISECONDS.toDays(startDate.timeInMillis)
        val week = days / 7 + 1

        currentWeek.value = "$formattedDate, Week $week"
    }

    override fun onCleared() {
        super.onCleared()
        dataSubscription.dispose()
        exceptionSubscription.dispose()
    }

    /**
     * We could use an extension property instead, but names would have to differ,
     * thus we're using a function so the names can correspond and naming is simpler.
     */
    fun summaryData(): LiveData<List<SummaryItem>> = summaryData

    fun studentName(): LiveData<String> = studentName

    fun currentWeek(): LiveData<String> = currentWeek

    fun refreshData() = summaryRefresher.randomiseSummary()

    /**
     * If there is a mid item (size == 3), remove one. If there isn't one check for bottom item.
     * If there is a bottom item (size > 1), remove one. If there isn't one (size = 1), set back to default (3);
     * preserving result count for the other item types.
     */
    fun adjustSize(midViewType: ViewType, bottomViewType: ViewType) {
        val oldSize =
                if (bottomViewType == CLASS_BOTTOM) {
                    (summaryData.value?.count { it.viewType == midViewType } ?: 0) + 1
                } else {
                    when {
                        summaryData.value?.find { it.viewType == midViewType } != null -> 3
                        summaryData.value?.find { it.viewType == bottomViewType } != null -> 2
                        else -> 1
                    }
                }

        val newSize = adjustListSize(oldSize)

        summaryData.value =
                when (bottomViewType) {
                    CLASS_BOTTOM -> {
                        createSummaryViewItems(backupClassSessions.take(newSize),
                                backupCarParks.take(summaryData.value?.count { it is CarParkItem } ?: 3),
                                backupShuttleBuses.take(summaryData.value?.count { it is ShuttleBusItem } ?: 3))
                    }
                    PARK_BOTTOM -> {
                        createSummaryViewItems(backupClassSessions.take(summaryData.value?.count { it is ClassSessionItem }
                                ?: 3),
                                backupCarParks.take(newSize),
                                backupShuttleBuses.take(summaryData.value?.count { it is ShuttleBusItem } ?: 3))
                    }
                    BUS_BOTTOM -> {
                        createSummaryViewItems(backupClassSessions.take(summaryData.value?.count { it is ClassSessionItem }
                                ?: 3),
                                backupCarParks.take(summaryData.value?.count { it is CarParkItem } ?: 3),
                                backupShuttleBuses.take(newSize))
                    }
                    else -> {
                        createSummaryViewItems(backupClassSessions,
                                backupCarParks,
                                backupShuttleBuses)
                    }
                }
    }

    // region Private Functions
    private fun createSummaryViewItems(classSessions: List<ClassSession>,
                                       carParks: List<CarPark>,
                                       shuttleBuses: List<ShuttleBus>) =
            mutableListOf<SummaryItem>().apply {
                add(ClassSessionTitleItem())
                addAll(classSessions.sortedBy { it.startTime }.toViewClassSessions())
                add(SectionTitleItem("Available car parks"))
                addAll(carParks.toViewCarParks())
                add(SectionTitleItem("Intercampus Shuttle Bus"))
                addAll(shuttleBuses.sortedBy { it.arrivalTime }.toViewShuttleBuses())
            }

    private fun List<ClassSession>.toViewClassSessions(): List<ClassSessionItem> {
        val size = this.size
        return mapIndexed { index, classSession ->
            ClassSessionItem(classSession.className, classSession.teacher, classSession.location,
                    formatSessionTime(classSession.startTime), formatSessionTime(classSession.endTime),
                    getClassViewType(index, size))
        }
    }

    private fun formatSessionTime(sessionTime: Int): String =
            sessionTime.let {
                val hours = if (it <= 12) it else it - 12
                val minutes = "00"
                val amPm = if (sessionTime <= 12) "AM" else "PM"

                "  $hours:$minutes  $amPm"
            }

    private fun List<CarPark>.toViewCarParks(): List<CarParkItem> {
        val size = this.size
        return mapIndexed { index, carPark ->
            CarParkItem("${carPark.location} live feed", carPark.availablePlaces.toString(),
                    getParkViewType(index, size))
        }
    }

    private fun List<ShuttleBus>.toViewShuttleBuses(): List<ShuttleBusItem> {
        val size = this.size
        return mapIndexed { index, bus ->
            ShuttleBusItem(bus.station, bus.destination, "${bus.arrivalTime} mins",
                    getBusViewType(index, size))
        }
    }

    private fun getClassViewType(position: Int, size: Int): ViewType =
            when (position) {
                size - 1 -> CLASS_BOTTOM
                in 0 until size - 1 -> CLASS_MID
                else -> ILLEGAL
            }

    private fun getParkViewType(position: Int, size: Int): ViewType =
            if (size == 1 && position == 0) PARK_SINGLE
            else when (position) {
                0 -> PARK_TOP
                size - 1 -> PARK_BOTTOM
                in 1 until size - 1 -> PARK_MID
                else -> ILLEGAL
            }

    private fun getBusViewType(position: Int, size: Int): ViewType =
            if (size == 1 && position == 0) BUS_SINGLE
            else when (position) {
                0 -> BUS_TOP
                size - 1 -> BUS_BOTTOM
                in 1 until size - 1 -> BUS_MID
                else -> ILLEGAL
            }

    private fun adjustListSize(currentSize: Int): Int =
            when (currentSize) {
                1 -> 3
                else -> currentSize - 1
            }
    // endregion Private Functions

    // region Screen Actions
    sealed class DisplayAction: Display.Action {
        data class ShowError(val error: StudentInfoException) : DisplayAction()
    }
    // endregion Screen Actions
}