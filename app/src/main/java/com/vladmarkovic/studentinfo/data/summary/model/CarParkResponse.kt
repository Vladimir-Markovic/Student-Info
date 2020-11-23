package com.vladmarkovic.studentinfo.domain.summary.model

import com.vladmarkovic.studentinfo.data.summary.model.Response

/**
 * Created by Vlad Markovic on 2019-09-10.
 */
data class CarParkResponse(val campus: String? = null,
                           val available: Int? = null): Response