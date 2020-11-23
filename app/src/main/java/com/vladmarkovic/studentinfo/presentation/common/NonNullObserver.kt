package com.vladmarkovic.studentinfo.presentation.common

import androidx.lifecycle.Observer

/**
 * Created by Vlad Markovic on 2019-09-11.
 *
 * Helper observer for removing boilerplate code when observing non null data.
 */
class NonNullObserver<T>(private val processData: (T) -> Unit) : Observer<T?> {

    override fun onChanged(data: T?) {
        data?.let { processData(it) }
    }
}