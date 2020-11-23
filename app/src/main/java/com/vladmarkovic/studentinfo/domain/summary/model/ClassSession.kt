package com.vladmarkovic.studentinfo.domain.summary.model

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
data class ClassSession(val className: String, val teacher: String, val location: String,
                        val startTime: Int, val endTime: Int)