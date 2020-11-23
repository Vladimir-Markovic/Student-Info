package com.vladmarkovic.studentinfo.application.injection.component

import android.app.Application
import com.vladmarkovic.studentinfo.application.StudentInfoApplication
import com.vladmarkovic.studentinfo.application.injection.module.ActivityModule
import com.vladmarkovic.studentinfo.application.injection.module.ApplicationModule
import com.vladmarkovic.studentinfo.application.injection.module.AppStateModule
import com.vladmarkovic.studentinfo.application.injection.module.DataModule
import com.vladmarkovic.studentinfo.application.injection.module.DomainModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton
import dagger.android.support.AndroidSupportInjectionModule

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class,
    ActivityModule::class, AppStateModule::class, DomainModule::class, DataModule::class])
interface ApplicationComponent : AndroidInjector<StudentInfoApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindApplication(application: Application): Builder

        fun build(): ApplicationComponent
    }
}