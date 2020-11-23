package com.vladmarkovic.studentinfo.application.injection.module

import com.vladmarkovic.studentinfo.application.injection.annotation.PerActivity
import com.vladmarkovic.studentinfo.presentation.summary.SummaryActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
@Module
internal abstract class ActivityModule {

    @PerActivity
    @ContributesAndroidInjector
    internal abstract fun injectSummaryActivity(): SummaryActivity
}