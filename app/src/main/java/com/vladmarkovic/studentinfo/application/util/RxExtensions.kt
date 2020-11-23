package com.vladmarkovic.studentinfo.application.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Vlad Markovic on 2019-09-09.
 */

fun <T> Observable<T>.subscribeOnIoThread(): Observable<T> = subscribeOn(Schedulers.io())

fun <T> Observable<T>.observeOnMainThread(delayError: Boolean = false): Observable<T> =
    observeOn(AndroidSchedulers.mainThread(), delayError)

fun <T> Observable<T>.subscribeIoObserveMain(delayError: Boolean = false): Observable<T> =
    subscribeOnIoThread().observeOnMainThread(delayError)