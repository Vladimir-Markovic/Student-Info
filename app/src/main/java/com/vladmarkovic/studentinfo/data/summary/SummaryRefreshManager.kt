package com.vladmarkovic.studentinfo.data.summary

import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.vladmarkovic.studentinfo.domain.summary.SummaryRefresher
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Created by Vlad Markovic on 2019-09-10.
 *
 * Used to control the FirestoreService. Often it might sit in the domain and operate
 * on the interface service is implementing, but here this just represents the mocking of
 * refreshing/resetting of the date in (using randomisation and timer reduction) in Firestore.
 */
@Singleton
class SummaryRefreshManager @Inject constructor(
        private val summaryService: SummaryService) : SummaryRefresher, LifecycleObserver {

    private companion object {
        private val DEFAULT_AVAILABLE_PARKING = listOf(
            Parking("Clayton", 645),
            Parking("Frankston", 212),
            Parking("City", 5)
        )

        private val DEFAULT_BUS_ARRIVALS = listOf(
            BusArrival("Caulfield", 4),
            BusArrival("Peninsula", 16),
            BusArrival("City", 12)
        )
    }

    private var availableParking = DEFAULT_AVAILABLE_PARKING.toList()
    private var busArrivals = DEFAULT_BUS_ARRIVALS.toList()

    private var timerSubscription: Disposable? = null

    init {
        resetAdjustTimer()
    }

    override fun randomiseSummary() {
        randomiseData()
        updateSummary()
    }

    override fun resetSummary() {
        availableParking = DEFAULT_AVAILABLE_PARKING.toList()
        busArrivals = DEFAULT_BUS_ARRIVALS.toList()
        updateSummary()
    }

    private fun updateSummary() {
        availableParking.forEach {
            summaryService.updateAvailableParking(it.location, it.available)
        }
        busArrivals.forEach {
            summaryService.updateBusArrival(it.destination, it.arrival)
        }
    }

    private fun randomiseData() {
        availableParking.forEach {
            it.available = Random.nextInt(it.available - 5, it.available + 5)
        }
        busArrivals.forEach {
            it.arrival = Random.nextInt(1, 30)
        }
    }

    /**
     * Adjust data every minute
     *  - available parking spots to random integer between -5 and +5 of current value
     *  - arrival time less one minute
     */
    private fun resetAdjustTimer() {
        timerSubscription?.dispose()
        timerSubscription = Observable.timer(1, MINUTES).subscribe {
            availableParking.forEach {
                it.available = Random.nextInt(it.available - 5, it.available + 5)
            }
            busArrivals.forEach {
                it.arrival = if (it.arrival == 1) 0 else it.arrival - 1
            }
            updateSummary()
        }
    }

    @OnLifecycleEvent(ON_DESTROY)
    fun onDestroy() {
        timerSubscription?.dispose()
    }

    private data class Parking(val location: String, var available: Int)
    private data class BusArrival(val destination: String, var arrival: Int)
}