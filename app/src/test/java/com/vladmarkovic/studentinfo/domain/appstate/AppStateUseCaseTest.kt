package com.vladmarkovic.studentinfo.domain.appstate

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.vladmarkovic.studentinfo.domain.appstate.model.AppState
import com.vladmarkovic.studentinfo.setRxSchedulersMainOnTrampoline
import io.reactivex.subjects.PublishSubject
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.subject.SubjectSpek
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
@RunWith(JUnitPlatform::class)
class AppStateUseCaseTest : SubjectSpek<AppStateUseCase>({

    setRxSchedulersMainOnTrampoline()

    lateinit var mockAppStatePublisher: PublishSubject<AppState>
    val appState = AppState()

    subject {
        mockAppStatePublisher = mock()

        AppStateUseCase(mockAppStatePublisher)
    }

    given("initial app state") {
        on("updating class sessions") {
            subject.updateClassSessions(classSessions)

            it("will update class sessions") {
                verify(mockAppStatePublisher).onNext(appState.copy(classSessions = classSessions))
            }
        }

        on("updating available parking") {
            subject.updateCarParks(availableParking)

            it("will update available parking") {
                verify(mockAppStatePublisher).onNext(appState.copy(carParks = availableParking))
            }
        }

        on("updating bus routes") {
            subject.updateShuttleBuses(busRoutes)

            it("will update bus routes") {
                verify(mockAppStatePublisher).onNext(appState.copy(shuttleBuses = busRoutes))
            }
        }
    }
})