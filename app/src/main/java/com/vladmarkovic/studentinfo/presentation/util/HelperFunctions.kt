package com.vladmarkovic.studentinfo.presentation.util

import android.content.Context
import android.widget.Toast

/**
 * Created by Vlad Markovic on 2019-09-12.
 */

fun toast(context: Context?, message: String, length: Int = Toast.LENGTH_SHORT) =
        context?.let { Toast.makeText(it,message, length).show() }
