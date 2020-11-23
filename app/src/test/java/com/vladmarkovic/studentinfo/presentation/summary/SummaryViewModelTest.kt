package com.vladmarkovic.studentinfo.presentation.summary

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.vladmarkovic.studentinfo.assertValueEquals
import com.vladmarkovic.studentinfo.domain.appstate.availableParking
import com.vladmarkovic.studentinfo.domain.appstate.busRoutes
import com.vladmarkovic.studentinfo.domain.appstate.classSessions
import com.vladmarkovic.studentinfo.domain.exception.StudentInfoException
import com.vladmarkovic.studentinfo.domain.summary.SummaryRefresher
import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBus
import com.vladmarkovic.studentinfo.domain.summary.model.ClassSession
import com.vladmarkovic.studentinfo.domain.summary.model.CarPark
import com.vladmarkovic.studentinfo.makeImmediateScheduler
import com.vladmarkovic.studentinfo.presentation.summary.SummaryViewModel.DisplayAction.ShowError
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.*
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.ViewType.*
import com.vladmarkovic.studentinfo.resetTestRxAndLiveData
import com.vladmarkovic.studentinfo.setTestRxAndLiveData
import com.vladmarkovic.studentinfo.testValue
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.CarParkItem as ViewParking
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.ShuttleBusItem as ViewBusRoute
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
@RunWith(JUnitPlatform::class)
class SummaryViewModelTest : SubjectSpek<SummaryViewModel>({

    lateinit var mockSummaryRefresher: SummaryRefresher
    val classSessionsObservable = BehaviorSubject.create<List<ClassSession>>()
    val availableParkingObservable = BehaviorSubject.create<List<CarPark>>()
    val busRoutesObservable = BehaviorSubject.create<List<ShuttleBus>>()
    val exceptionObservable = BehaviorSubject.create<StudentInfoException>()

    subject {
        mockSummaryRefresher = mock()

        SummaryViewModel(mockSummaryRefresher, classSessionsObservable, availableParkingObservable,
                busRoutesObservable, exceptionObservable)
    }

    beforeGroup { setTestRxAndLiveData(makeImmediateScheduler()); subject }
    afterGroup { resetTestRxAndLiveData(subject) }

    given("monitoring data on the summary screen") {
        beforeGroup { subject }

        on("new class sessions, parking and bus route data") {
            classSessionsObservable.onNext(classSessions)
            availableParkingObservable.onNext(availableParking)
            busRoutesObservable.onNext(busRoutes)

            it("displays new class session, parking and bus route data") {
                val sortedViewClassSessions = listOf(
                        ClassSessionItem("Class1", "Teacher1", "Location1", "  11:00  AM", "  12:00  AM", CLASS_MID),
                        ClassSessionItem("Class2", "Teacher2", "Location2", "  1:00  PM", "  2:00  PM", CLASS_MID),
                        ClassSessionItem("Class3", "Teacher3", "Location3", "  3:00  PM", "  4:00  PM", CLASS_BOTTOM))

                val viewAvailableParking = listOf(ViewParking("Clayton live feed", "245", PARK_TOP),
                        ViewParking("Frankston live feed", "212", PARK_MID),
                        ViewParking("City live feed", "5", PARK_BOTTOM))

                val sortedViewBusRoutes = listOf(ViewBusRoute("Clayton", "Frankston", "4 mins", BUS_TOP),
                        ViewBusRoute("Clayton", "City", "8 mins", BUS_MID),
                        ViewBusRoute("Clayton", "Peninsula", "12 mins", BUS_BOTTOM))

                val viewSummaryData = mutableListOf<SummaryItem>().apply {
                    add(ClassSessionTitleItem())
                    addAll(sortedViewClassSessions)
                    add(SectionTitleItem("Available car parks"))
                    addAll(viewAvailableParking)
                    add(SectionTitleItem("Intercampus Shuttle Bus"))
                    addAll(sortedViewBusRoutes)
                }

                subject.summaryData().assertValueEquals(viewSummaryData)
            }
        }

        on("refreshing the summary data") {
            subject.refreshData()

            it("refreshes to new random summary data") {
                verify(mockSummaryRefresher).randomiseSummary()
            }
        }
    }

    given("an exception occurred") {
        on("new exception") {
            val newException = StudentInfoException("An error occurred.")
            exceptionObservable.onNext(newException)

            it("shows exception error message") {
                assertEquals(subject.displayActions.testValue, ShowError(newException))
            }
        }
    }
})