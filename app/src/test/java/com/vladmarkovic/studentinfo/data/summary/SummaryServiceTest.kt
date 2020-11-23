package com.vladmarkovic.studentinfo.data.summary

import com.google.firebase.firestore.CollectionReference
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.vladmarkovic.studentinfo.data.FirestoreReferenceManager
import com.vladmarkovic.studentinfo.domain.appstate.AppStateUseCase
import com.vladmarkovic.studentinfo.domain.exception.StudentInfoException
import io.reactivex.processors.PublishProcessor
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * Created by Vlad Markovic on 2019-09-10.
 */
@RunWith(JUnitPlatform::class)
class SummaryServiceTest : SubjectSpek<SummaryService>({

    lateinit var mockFirestoreReferenceManager: FirestoreReferenceManager
    lateinit var mockAppStateUseCase: AppStateUseCase

    lateinit var mockClassSessionsCollectionReference: CollectionReference
    lateinit var mockCarParksCollectionReference: CollectionReference
    lateinit var mockBusesCollectionReferencer: CollectionReference
    lateinit var mockExceptionPublisher: PublishProcessor<StudentInfoException>

    val campusField = "campus"
    val campus = "Clayton"
    val destinationField = "destination"
    val destination = "Caulfield"

    subject {
        mockFirestoreReferenceManager = mock()
        mockAppStateUseCase = mock()

        mockClassSessionsCollectionReference = mock()
        mockCarParksCollectionReference = mock()
        mockBusesCollectionReferencer = mock()

        mockExceptionPublisher = mock()

        whenever(mockFirestoreReferenceManager.setUp()).thenReturn(true)

        whenever(mockFirestoreReferenceManager.classSessionsCollection).thenReturn(mockClassSessionsCollectionReference)
        whenever(mockFirestoreReferenceManager.carParksCollection).thenReturn(mockCarParksCollectionReference)
        whenever(mockFirestoreReferenceManager.shuttleBusesCollection).thenReturn(mockBusesCollectionReferencer)

        SummaryService(mockFirestoreReferenceManager, mockAppStateUseCase, mockExceptionPublisher)
    }

    given("summary data observing") {
        on("start") {
            subject

            it("sets up collection references") {
                verify(mockFirestoreReferenceManager).setUp()
            }

            it("sets up collection listeners") {
                verify(mockClassSessionsCollectionReference).addSnapshotListener(any())
                verify(mockCarParksCollectionReference).addSnapshotListener(any())
                verify(mockBusesCollectionReferencer).addSnapshotListener(any())
            }
        }
    }

    given("summary data updating") {
        on("updating available parking data") {
            subject.updateAvailableParking(campus, 87)

            it("does update available parking data") {
                verify(mockCarParksCollectionReference).whereEqualTo(campusField, campus)
            }
        }

        on("updating bus arrival data") {
            subject.updateBusArrival(destination, 2)

            it("does update bus arrival data") {
                verify(mockBusesCollectionReferencer).whereEqualTo(destinationField, destination)
            }
        }
    }

    given("summary service tear down") {
        on("clearing resources") {
            subject.onDestroy()

            it("clears the collection references") {
                verify(mockFirestoreReferenceManager).clear()
            }
        }
    }
})