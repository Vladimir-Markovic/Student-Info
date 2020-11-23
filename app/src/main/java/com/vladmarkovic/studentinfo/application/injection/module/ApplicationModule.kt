package com.vladmarkovic.studentinfo.application.injection.module

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
@Module
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindApplicationContext(application: Application): Context
}