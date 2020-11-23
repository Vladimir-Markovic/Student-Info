package com.vladmarkovic.studentinfo.data

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vlad Markovic on 2019-09-10.
 *
 * Uses FirestoreFirebase to get a get a hold of Firestore collection references
 *
 * In this case it is simple set-up on initialisation, it could have been self contained
 * (call setup from init here), but this is not common in practice - usually concerns are split:
 * this class knows what is needs to do and how to do it, and it is controlled from outside -
 * some other class determines when to setup usually dependant on some logic or event, like when
 * the connection is established.
 */
@Singleton
class FirestoreReferenceManager @Inject constructor(private val firestore: FirebaseFirestore) {

    private companion object {
        private const val CLASSES_COLLECTION_ID = "classes"
        private const val CAR_PARKS_COLLECTION_ID = "carparks"
        private const val BUSES_COLLECTION_ID = "buses"
    }

    var classSessionsCollection: CollectionReference? = null
        private set
    var carParksCollection: CollectionReference? = null
        private set
    var shuttleBusesCollection: CollectionReference? = null
        private set

    var areReferencesSet: Boolean = false
        private set

    fun setUp(): Boolean {
        areReferencesSet = setReferences()
        return areReferencesSet
    }

    fun clear() {
        areReferencesSet = false
        classSessionsCollection = null
        carParksCollection = null
        shuttleBusesCollection = null
    }

    private fun setReferences(): Boolean {
        classSessionsCollection = firestore.collection(CLASSES_COLLECTION_ID)
        carParksCollection = firestore.collection(CAR_PARKS_COLLECTION_ID)
        shuttleBusesCollection = firestore.collection(BUSES_COLLECTION_ID)

        areReferencesSet = classSessionsCollection != null && carParksCollection != null && shuttleBusesCollection != null

        if (!areReferencesSet) clear()

        return areReferencesSet
    }
}