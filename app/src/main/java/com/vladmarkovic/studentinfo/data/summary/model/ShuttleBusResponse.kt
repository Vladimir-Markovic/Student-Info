package com.vladmarkovic.studentinfo.domain.summary.model

import com.vladmarkovic.studentinfo.data.summary.model.Response

/**
 * Created by Vlad Markovic on 2019-09-10.
 */
data class ShuttleBusResponse(val station: String? = null,
                              val destination: String? = null,
                              val arrival: Int? = null): Response