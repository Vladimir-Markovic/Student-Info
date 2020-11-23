package com.vladmarkovic.studentinfo.application.injection.module

import com.vladmarkovic.studentinfo.application.util.subscribeIoObserveMain
import com.vladmarkovic.studentinfo.domain.appstate.model.AppState
import com.vladmarkovic.studentinfo.domain.exception.StudentInfoException
import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBus
import com.vladmarkovic.studentinfo.domain.summary.model.ClassSession
import com.vladmarkovic.studentinfo.domain.summary.model.CarPark
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.PublishSubject
import javax.inject.Singleton

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
@Module
class AppStateModule {

    @Singleton
    @Provides
    fun provideAppStatePublisher(): PublishSubject<AppState> = PublishSubject.create()

    @Singleton
    @Provides
    fun provideAppStateObservable(publisher: PublishSubject<AppState>): Observable<AppState> =
            publisher.serialize().publish().autoConnect().subscribeIoObserveMain()


    @Provides
    fun provideClassSessionsObservable(appStateObservable: Observable<AppState>)
            : Observable<List<ClassSession>> = appStateObservable
            .map { it.classSessions.toList() }
            .distinctUntilChanged()

    @Provides
    fun provideParkingsObservable(appStateObservable: Observable<AppState>)
            : Observable<List<CarPark>> = appStateObservable
            .map { it.carParks.toList() }
            .distinctUntilChanged()

    @Provides
    fun provideBusRoutesObservable(appStateObservable: Observable<AppState>):
            Observable<List<ShuttleBus>> = appStateObservable
            .map { it.shuttleBuses.toList() }
            .distinctUntilChanged()

    @Provides
    @Singleton
    fun provideExceptionPublisher(): PublishProcessor<StudentInfoException> = PublishProcessor.create()

    @Singleton
    @Provides
    fun provideExceptionObservable(publisher: PublishProcessor<StudentInfoException>)
            : Observable<StudentInfoException> =
            publisher.toObservable().serialize().publish().autoConnect().subscribeIoObserveMain()
}