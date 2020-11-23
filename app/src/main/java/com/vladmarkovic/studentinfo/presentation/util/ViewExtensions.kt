package com.vladmarkovic.studentinfo.presentation.util

import android.view.View
import androidx.annotation.IdRes
import kotlin.LazyThreadSafetyMode.NONE

/**
 * Created by Vlad Markovic on 2019-09-11.
 *
 * Helper function for removing boilerplate code for findViewById when binding views.
 * (Synthetic binding can often cause problems if modular code or custom resource sets are used).
 */

fun <T : View> View.bind(@IdRes res: Int): Lazy<T> = lazy(NONE) { findViewById<T>(res)!! }