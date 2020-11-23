package com.vladmarkovic.studentinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Created by Vlad Markovic on 2019-09-09.
 */

val <T> LiveData<T>.testValue: T?
    get() {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { newValue ->
            value = newValue
            latch.countDown()
        }

        observeForever(observer)

        latch.await(1, TimeUnit.SECONDS) // Max await time

        removeObserver(observer)

        return value
    }

fun <T> LiveData<T>.assertValueEquals(other: T?, message: String = "${this.testValue} == $other?") {
    assertEquals(message, other, this.testValue)
}