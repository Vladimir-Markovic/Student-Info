package com.vladmarkovic.studentinfo.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by Vlad Markovic on 2019-09-10.
 */
@RunWith(JUnitPlatform::class)
class FirestoreReferenceManagerTest : SubjectSpek<FirestoreReferenceManager>({

    lateinit var mockFirestore: FirebaseFirestore
    lateinit var mockClassesCollectionReference: CollectionReference
    lateinit var mockCarParksCollectionReference: CollectionReference
    lateinit var mockBusesCollectionReference: CollectionReference

    val CLASSES_COLLECTION_ID = "classes"
    val CAR_PARKS_COLLECTION_ID = "carparks"
    val BUSES_COLLECTION_ID = "buses"

    subject {
        mockFirestore = mock()
        mockClassesCollectionReference = mock()
        mockCarParksCollectionReference = mock()
        mockBusesCollectionReference = mock()

        whenever(mockFirestore.collection(CLASSES_COLLECTION_ID)).thenReturn(mockClassesCollectionReference)
        whenever(mockFirestore.collection(CAR_PARKS_COLLECTION_ID)).thenReturn(mockCarParksCollectionReference)
        whenever(mockFirestore.collection(BUSES_COLLECTION_ID)).thenReturn(mockBusesCollectionReference)

        FirestoreReferenceManager(mockFirestore)
    }

    given("Firestore reference setup") {
        on("setting up references") {
            subject.setUp()

            it("sets up Firestore collection references") {
                verify(mockFirestore).collection(CLASSES_COLLECTION_ID)
                verify(mockFirestore).collection(CAR_PARKS_COLLECTION_ID)
                verify(mockFirestore).collection(BUSES_COLLECTION_ID)

                assertTrue(subject.areReferencesSet)

                assertEquals(mockClassesCollectionReference, subject.classSessionsCollection)
                assertEquals(mockCarParksCollectionReference, subject.carParksCollection)
                assertEquals(mockBusesCollectionReference, subject.shuttleBusesCollection)
            }
        }

        on("clearing references") {
            subject.clear()

            it("does clear the references") {
                assertEquals(null, subject.classSessionsCollection)
                assertEquals(null, subject.carParksCollection)
                assertEquals(null, subject.shuttleBusesCollection)
            }
        }
    }
})