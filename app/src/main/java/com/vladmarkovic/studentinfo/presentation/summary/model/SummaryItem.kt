package com.vladmarkovic.studentinfo.presentation.summary.model

import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.ViewType.CLASS_TITLE
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.ViewType.SECTION_TITLE

/**
 * Created by Vlad Markovic on 2019-09-09.
 *
 * View model exactly modeling the way data needs to be represented in the view,
 * prepared in the view model, so the view can be free of all logic.
 */
sealed class SummaryItem(open val viewType: ViewType) {

    data class ClassSessionTitleItem(override val viewType: ViewType = CLASS_TITLE) : SummaryItem(viewType)

    data class ClassSessionItem(val classTitle: String,
                                val teacher: String,
                                val location: String,
                                val startTime: String,
                                val endTime: String,
                                override val viewType: ViewType) : SummaryItem(viewType)

    data class CarParkItem(val title: String,
                           val availablePlaces: String,
                           override val viewType: ViewType) : SummaryItem(viewType)

    data class ShuttleBusItem(val station: String,
                              val destination: String,
                              val arrival: String,
                              override val viewType: ViewType) : SummaryItem(viewType)

    data class SectionTitleItem(val title: String,
                                override val viewType: ViewType = SECTION_TITLE)
        : SummaryItem(viewType)

    enum class ViewType(val id: Int) {
        CLASS_TITLE(0), CLASS_MID(1), CLASS_BOTTOM(2),
        SECTION_TITLE(3),
        PARK_SINGLE(4), PARK_TOP(5), PARK_MID(6), PARK_BOTTOM(7),
        BUS_SINGLE(8), BUS_TOP(9), BUS_MID(10), BUS_BOTTOM(11),
        ILLEGAL(-1);
    }
}