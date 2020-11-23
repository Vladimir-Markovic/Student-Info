package com.vladmarkovic.studentinfo.domain.appstate

import com.vladmarkovic.studentinfo.domain.appstate.model.AppState
import com.vladmarkovic.studentinfo.domain.summary.model.CarPark
import com.vladmarkovic.studentinfo.domain.summary.model.ClassSession
import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBus
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vlad Markovic on 2019-09-09.
 *
 * A wrapper around an app state used host app state and for applying and publishing changes,
 * preventing direct mutability of the app state.
 */
@Singleton
class AppStateUseCase @Inject constructor(private val appStatePublisher: PublishSubject<AppState>) {

    private val appState = AppState()

    fun updateClassSessions(classSessions: List<ClassSession>) {
        appState.classSessions = classSessions
        appStatePublisher.onNext(appState)
    }

    fun updateCarParks(availableParking: List<CarPark>) {
        appState.carParks = availableParking
        appStatePublisher.onNext(appState)
    }

    fun updateShuttleBuses(shuttleBuses: List<ShuttleBus>) {
        appState.shuttleBuses = shuttleBuses
        appStatePublisher.onNext(appState)
    }
}