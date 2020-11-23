package com.vladmarkovic.studentinfo.domain.summary

/**
 * Created by Vlad Markovic on 2019-09-10.
 *
 * Abstracts the SummaryRefreshManager for randomising and refreshing data in Firestore, modeling change.
 */
interface SummaryRefresher {

    fun randomiseSummary()
    fun resetSummary()
}