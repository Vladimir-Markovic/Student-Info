package com.vladmarkovic.studentinfo.domain.appstate

import com.vladmarkovic.studentinfo.domain.summary.model.ShuttleBus
import com.vladmarkovic.studentinfo.domain.summary.model.ClassSession
import com.vladmarkovic.studentinfo.domain.summary.model.CarPark

/**
 * Created by Vlad Markovic on 2019-09-09.
 */

val classSessions = listOf(
        ClassSession("Class2", "Teacher2", "Location2", 13, 14),
        ClassSession("Class3", "Teacher3", "Location3", 15, 16),
        ClassSession("Class1", "Teacher1", "Location1", 11, 12)
)

val availableParking = listOf(CarPark("Clayton", 245), CarPark("Frankston", 212), CarPark("City", 5))

val busRoutes = listOf(
        ShuttleBus("Clayton", "City", 8),
        ShuttleBus("Clayton", "Peninsula", 12),
        ShuttleBus("Clayton", "Frankston", 4)
)