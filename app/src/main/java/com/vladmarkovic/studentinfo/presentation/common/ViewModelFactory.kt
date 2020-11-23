package com.vladmarkovic.studentinfo.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by Vlad Markovic on 2019-09-09.
 *
 * Helper class to remove boilerplate code for having to write factory for each view model.
 */
class ViewModelFactory<T : ViewModel> @Inject constructor(private val viewModel: Lazy<T>) :
        ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel.get() as T
}