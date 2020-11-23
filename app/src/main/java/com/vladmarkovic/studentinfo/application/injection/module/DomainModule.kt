package com.vladmarkovic.studentinfo.application.injection.module

import com.vladmarkovic.studentinfo.data.summary.SummaryRefreshManager
import com.vladmarkovic.studentinfo.domain.summary.SummaryRefresher
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created by Vlad Markovic on 2019-09-11.
 */
@Module
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindSummaryRefresher(summaryRefreshManager: SummaryRefreshManager): SummaryRefresher
}