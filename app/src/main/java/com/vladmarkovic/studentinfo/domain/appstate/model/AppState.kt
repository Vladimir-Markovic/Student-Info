package com.vladmarkovic.studentinfo.domain.appstate.model

import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBus
import com.vladmarkovic.studentinfo.domain.summary.model.ClassSession
import com.vladmarkovic.studentinfo.domain.summary.model.CarPark

/**
 * Created by Vlad Markovic on 2019-09-09.
 *
 * Properties are mutable, but can only be mutated via the use case and not directly.
 */
data class AppState(var classSessions: List<ClassSession> = listOf(),
                    var carParks: List<CarPark> = listOf(),
                    var shuttleBuses: List<ShuttleBus> = listOf())