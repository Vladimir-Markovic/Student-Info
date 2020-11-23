package com.vladmarkovic.studentinfo.domain.summary.model

import com.google.firebase.firestore.PropertyName
import com.vladmarkovic.studentinfo.data.summary.model.Response

/**
 * Created by Vlad Markovic on 2019-09-10.
 */
data class ClassSessionResponse(@get:PropertyName("class") @set:PropertyName("class")
                                var className: String? = null,
                                val teacher: String? = null,
                                val location: String? = null,
                                val start: Int? = null,
                                val end: Int? = null) : Response