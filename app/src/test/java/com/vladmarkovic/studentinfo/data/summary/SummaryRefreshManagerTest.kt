package com.vladmarkovic.studentinfo.data.summary

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBus
import com.vladmarkovic.studentinfo.domain.summary.model.CarPark
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * Created by Vlad Markovic on 2019-09-10.
 */
@RunWith(JUnitPlatform::class)
class SummaryRefreshManagerTest : SubjectSpek<SummaryRefreshManager>({

    lateinit var mockSummaryService: SummaryService

    subject {
        mockSummaryService = mock()

        SummaryRefreshManager(mockSummaryService)
    }

    given("default summary data") {
        on("resetting summary data") {
            subject.resetSummary()

            it("will reset the summary data to defaults") {
                val defaultAvailableParking = listOf(
                        CarPark("Clayton", 645),
                        CarPark("Frankston", 212),
                        CarPark("City", 5)
                )

                val defaultBusArrivals = listOf(
                        ShuttleBus("Clayton", "Caulfield", 4),
                        ShuttleBus("Clayton", "Peninsula", 16),
                        ShuttleBus("Clayton", "City", 12)
                )

                defaultAvailableParking.forEach {
                    verify(mockSummaryService).updateAvailableParking(it.location, it.availablePlaces)
                }

                defaultBusArrivals.forEach {
                    verify(mockSummaryService).updateBusArrival(it.destination, it.arrivalTime)
                }
            }
        }
    }
})