package com.vladmarkovic.studentinfo.application

import com.vladmarkovic.studentinfo.BuildConfig
import com.vladmarkovic.studentinfo.application.injection.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
class StudentInfoApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder()
            .bindApplication(this)
            .build()

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}