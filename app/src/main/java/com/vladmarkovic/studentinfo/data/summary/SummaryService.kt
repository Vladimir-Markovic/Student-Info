package com.vladmarkovic.studentinfo.data.summary

import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.vladmarkovic.studentinfo.data.FirestoreReferenceManager
import com.vladmarkovic.studentinfo.data.summary.model.Response
import com.vladmarkovic.studentinfo.domain.appstate.AppStateUseCase
import com.vladmarkovic.studentinfo.domain.exception.StudentInfoException
import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBus
import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBusResponse
import com.vladmarkovic.studentinfo.domain.summary.model.CarParkResponse
import com.vladmarkovic.studentinfo.domain.summary.model.ClassSession
import com.vladmarkovic.studentinfo.domain.summary.model.ClassSessionResponse
import com.vladmarkovic.studentinfo.domain.summary.model.CarPark
import io.reactivex.processors.PublishProcessor
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vlad Markovic on 2019-09-10.
 */
@Singleton
class SummaryService @Inject constructor(
        private val firestoreReferenceManager: FirestoreReferenceManager,
        private val appStateUseCase: AppStateUseCase,
        private val exceptionPublisher: PublishProcessor<StudentInfoException>) : LifecycleObserver {

    private fun getClassesCollectionReference() = firestoreReferenceManager.classSessionsCollection
    private fun getCarParksCollectionReference() = firestoreReferenceManager.carParksCollection
    private fun getBusesCollectionReference() = firestoreReferenceManager.shuttleBusesCollection

    private val listeners = listOf<ResponseListener<*>>(
            ResponseListener(ClassSessionResponse::class.java, ::getClassesCollectionReference),
            ResponseListener(CarParkResponse::class.java, ::getCarParksCollectionReference),
            ResponseListener(ShuttleBusResponse::class.java, ::getBusesCollectionReference)
    )

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        firestoreReferenceManager.setUp()

        addListeners()
    }

    fun updateAvailableParking(campus: String, availableSpots: Int) {
        update(getCarParksCollectionReference(), "campus", campus, "available", availableSpots)
    }

    fun updateBusArrival(destination: String, arrival: Int) {
        update(getBusesCollectionReference(), "destination", destination, "arrival", arrival)
    }

    private fun update(collection: CollectionReference?,
               findByFieldId: String, findByValue: String,
               updateFieldId: String, updateValue: Any) {

        collection?.whereEqualTo(findByFieldId, findByValue)?.get()
                ?.addOnSuccessListener { documents ->
                    documents.firstOrNull()?.reference?.update(updateFieldId, updateValue)
                }?.addOnFailureListener { exception ->
                    exceptionPublisher.onNext(StudentInfoException(exception.message, exception.cause))
                }
    }

    private fun removeListeners() {
        listeners.forEach {
            it.registration?.remove()
            it.registration = null
        }
    }

    private fun addListeners() {
        listeners.forEach { listener ->
            if (listener.registration == null) {
                listener.registration =
                        listener.collectionReference.invoke()
                                ?.addSnapshotListener { snapshot, firestoreException ->

                                    firestoreException?.let {
                                        exceptionPublisher.onNext(StudentInfoException(it.message, it.cause))
                                    } ?: run {
                                        try {
                                            snapshot?.documents?.mapNotNull {
                                                it?.toResponse(listener.id, listener.javaClass)
                                            }?.process()
                                        } catch (exception: Exception) {
                                            exceptionPublisher.onNext(StudentInfoException(exception))
                                        }
                                    }
                                }
            }
        }
    }

    private fun DocumentSnapshot.toResponse(id: String, javaClass: Class<out Response>): Response? =
            when (id) {
                ClassSessionResponse::class.java.simpleName -> this.toObject(javaClass) as ClassSessionResponse
                CarParkResponse::class.java.simpleName -> this.toObject(javaClass) as CarParkResponse
                ShuttleBusResponse::class.java.simpleName -> this.toObject(javaClass) as ShuttleBusResponse
                else -> null
            }

    @Suppress("UNCHECKED_CAST")
    private fun List<Response>?.process() =
            if (this == null) Unit
            else {
                when (this.firstOrNull()) {
                    is ClassSessionResponse -> {
                        appStateUseCase.updateClassSessions((this as List<ClassSessionResponse>).mapToDomainClassSessions())
                    }
                    is CarParkResponse -> {
                        appStateUseCase.updateCarParks((this as List<CarParkResponse>).mapToDomainCarParks())
                    }
                    is ShuttleBusResponse -> {
                        appStateUseCase.updateShuttleBuses((this as List<ShuttleBusResponse>).mapToDomainShuttleBuses())
                    }
                    else -> {
                        exceptionPublisher.onNext(StudentInfoException("Data list was empty."))
                    }
                }
            }

    private fun List<ClassSessionResponse>.mapToDomainClassSessions(): List<ClassSession> =
            this.mapNotNull { it.toDomain() }

    private fun List<CarParkResponse>.mapToDomainCarParks(): List<CarPark> =
            this.mapNotNull { it.toDomain() }

    private fun List<ShuttleBusResponse>.mapToDomainShuttleBuses(): List<ShuttleBus> =
            this.mapNotNull { it.toDomain() }

    private fun ClassSessionResponse.toDomain(): ClassSession? =
            if (className == null || start == null) null
            else ClassSession(className!!, teacher ?: "", location ?: "", start, end ?: -1)

    private fun CarParkResponse.toDomain(): CarPark? =
            if (campus == null || available == null) null
            else CarPark(campus, available)

    private fun ShuttleBusResponse.toDomain(): ShuttleBus? =
            if (station == null || destination == null || arrival == null) null
            else ShuttleBus(station, destination, arrival)

    private data class ResponseListener<T : Response>(
            val javaClass: Class<T>,
            val collectionReference: (() -> CollectionReference?),
            var registration: ListenerRegistration? = null) {

        val id: String get() = javaClass.simpleName
    }

    @OnLifecycleEvent(ON_DESTROY)
    fun onDestroy() {
        removeListeners()
        firestoreReferenceManager.clear()
    }
}