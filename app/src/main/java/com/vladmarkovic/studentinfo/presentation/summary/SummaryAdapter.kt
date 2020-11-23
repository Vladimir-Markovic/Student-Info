package com.vladmarkovic.studentinfo.presentation.summary

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vladmarkovic.studentinfo.R
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.*
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.ViewType.*
import com.vladmarkovic.studentinfo.presentation.util.bind

/**
 * Created by Vlad Markovic on 2019-09-11.
 * Adapter as a view is completely "dumb".
 * Uses multiple ViewHolders representing all different item types in the list
 * for different layouts and backgrounds including applying blurred shadows with 9-patch images.
 */
class SummaryAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var itemList: MutableList<SummaryItem> = mutableListOf()

    var classItemClickListener: (() -> Unit)? = null
    var carParkItemClickListener: (() -> Unit)? = null
    var shuttleBusItemClickListener: (() -> Unit)? = null

    override fun getItemViewType(position: Int) = itemList[position].viewType.id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            inflateLayoutForViewHolder(parent, viewType)

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemList[position].let { item ->
            when {
                holder is ClassSessionTitleViewHolder -> Unit // nothing to bind
                holder is HeadingViewHolder && item is SectionTitleItem -> holder.bind(item)
                holder is ClassSessionsViewHolder && item is ClassSessionItem -> holder.bind(item)
                holder is CarParkViewHolder && item is CarParkItem -> holder.bind(item)
                holder is ShuttleBusViewHolder && item is ShuttleBusItem -> holder.bind(item)
            }
        }
    }

    fun setItems(items: List<SummaryItem>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    private fun inflateLayoutForViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            when (viewType) {
                CLASS_TITLE.id -> {
                    ClassSessionTitleViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.class_session_title_item, parent, false), classItemClickListener)
                }
                CLASS_MID.id -> {
                    ClassSessionMidViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.class_session_item, parent, false), classItemClickListener)
                }
                CLASS_BOTTOM.id -> {
                    ClassSessionBottomViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.class_session_item, parent, false), classItemClickListener)
                }
                SECTION_TITLE.id -> {
                    HeadingViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.section_title_item, parent, false))
                }
                PARK_SINGLE.id -> {
                    CarParkSingleViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.car_park_item, parent, false), carParkItemClickListener)
                }
                PARK_TOP.id -> {
                    CarParkTopViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.car_park_item, parent, false), carParkItemClickListener)
                }
                PARK_MID.id -> {
                    CarParkMidViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.car_park_item, parent, false), carParkItemClickListener)
                }
                PARK_BOTTOM.id -> {
                    CarParkBottomViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.car_park_item, parent, false), carParkItemClickListener)
                }
                BUS_SINGLE.id -> {
                    ShuttleBusSingleViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.shuttle_bus_item, parent, false), shuttleBusItemClickListener)
                }
                BUS_TOP.id -> {
                    ShuttleBusTopViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.shuttle_bus_item, parent, false), shuttleBusItemClickListener)
                }
                BUS_MID.id -> {
                    ShuttleBusMidViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.shuttle_bus_item, parent, false), shuttleBusItemClickListener)
                }
                BUS_BOTTOM.id -> {
                    ShuttleBusBottomViewHolder(LayoutInflater.from(parent.context)
                            .inflate(R.layout.shuttle_bus_item, parent, false), shuttleBusItemClickListener)
                }
                else -> throw IllegalArgumentException("Illegal summary item type")
            }

    // region ViewHolder
    abstract class SummaryViewHolder(view: View) : RecyclerView.ViewHolder(view), Item {

        fun setItemLayoutBackgroundResource() {
            getBackgroundResource()?.let { layout.setBackgroundResource(it) }
        }
    }

    // region ClassSessionsViewHolder
    class ClassSessionTitleViewHolder(view: View, clickListener: (() -> Unit)?) : SummaryViewHolder(view), TopItem {

        override fun getBackgroundResource(): Int = R.drawable.bgd_item_top_no_padding
        override val layout by view.bind<ViewGroup>(R.id.classTitleItemLayout)

        init {
            setItemLayoutBackgroundResource()
            clickListener?.let { view.setOnClickListener { clickListener.invoke() } }
        }
    }

    abstract class ClassSessionsViewHolder(view: View, clickListener: (() -> Unit)?) : SummaryViewHolder(view) {
        override val layout by view.bind<ViewGroup>(R.id.classSessionItemLayout)

        init {
            setItemLayoutBackgroundResource()
            clickListener?.let { view.setOnClickListener { clickListener.invoke() } }
        }

        private val classStartTime by view.bind<TextView>(R.id.classStartTime)
        private val classEndTime by view.bind<TextView>(R.id.classEndTime)
        private val classTitle by view.bind<TextView>(R.id.classTitle)
        private val classTeacher by view.bind<TextView>(R.id.classTeacher)
        private val classLocation by view.bind<TextView>(R.id.classLocation)
        private val itemDivider by view.bind<View>(R.id.itemDivider)

        fun bind(classSessionItem: ClassSessionItem) {
            classStartTime.text = classSessionItem.startTime
            classEndTime.text = classSessionItem.endTime
            classTitle.text = classSessionItem.classTitle
            classTeacher.text = classSessionItem.teacher
            classLocation.text = classSessionItem.location
            itemDivider.visibility = if (showDivider) VISIBLE else GONE
        }
    }

    class ClassSessionMidViewHolder(view: View, clickListener: (() -> Unit)?)
        : ClassSessionsViewHolder(view, clickListener), MidItem
    class ClassSessionBottomViewHolder(view: View, clickListener: (() -> Unit)?)
        : ClassSessionsViewHolder(view, clickListener), BottomItem

    class HeadingViewHolder(view: View) : SummaryViewHolder(view), HeadingItem {
        override val layout by view.bind<ViewGroup>(R.id.sectionTitleItemLayout)

        init {
            setItemLayoutBackgroundResource()
        }

        private val sectionTitle by view.bind<TextView>(R.id.sectionTitle)

        fun bind(sectionTitleItem: SectionTitleItem) {
            sectionTitle.text = sectionTitleItem.title
        }
    }
    // endregion ClassSessionsViewHolder

    // region CarParkViewHolder
    abstract class CarParkViewHolder(view: View, clickListener: (() -> Unit)?) : SummaryViewHolder(view) {

        override val layout by view.bind<ViewGroup>(R.id.carParkItemLayout)

        init {
            setItemLayoutBackgroundResource()
            clickListener?.let { view.setOnClickListener { clickListener.invoke() } }
        }

        private val carParkTitle by view.bind<TextView>(R.id.carParkName)
        private val availableParkSpots by view.bind<TextView>(R.id.availableParkSpots)
        private val itemDivider by view.bind<View>(R.id.itemDivider)

        fun bind(carParkItem: CarParkItem) {
            carParkTitle.text = carParkItem.title
            availableParkSpots.text = carParkItem.availablePlaces
            itemDivider.visibility = if (showDivider) VISIBLE else GONE
        }
    }

    class CarParkSingleViewHolder(view: View, clickListener: (() -> Unit)?)
        : CarParkViewHolder(view, clickListener), SingleItem
    class CarParkTopViewHolder(view: View, clickListener: (() -> Unit)?)
        : CarParkViewHolder(view, clickListener), TopItem
    class CarParkMidViewHolder(view: View, clickListener: (() -> Unit)?)
        : CarParkViewHolder(view, clickListener), MidItem
    class CarParkBottomViewHolder(view: View, clickListener: (() -> Unit)?)
        : CarParkViewHolder(view, clickListener), BottomItem
    // endregion CarParkViewHolder

    // region ShuttleBusViewHolder
    abstract class ShuttleBusViewHolder(view: View, clickListener: (() -> Unit)?) : SummaryViewHolder(view) {

        override val layout by view.bind<ViewGroup>(R.id.shuttleBusItemLayout)

        init {
            setItemLayoutBackgroundResource()
            clickListener?.let { view.setOnClickListener { clickListener.invoke() } }
        }

        private val busStation by view.bind<TextView>(R.id.busStation)
        private val busDestination by view.bind<TextView>(R.id.busDestination)
        private val busArrivalTime by view.bind<TextView>(R.id.busArrivalTime)
        private val itemDivider by view.bind<View>(R.id.itemDivider)

        fun bind(shuttleBusItem: ShuttleBusItem) {
            busStation.text = shuttleBusItem.station
            busDestination.text = shuttleBusItem.destination
            busArrivalTime.text = shuttleBusItem.arrival
            itemDivider.visibility = if (showDivider) VISIBLE else GONE
        }
    }

    class ShuttleBusSingleViewHolder(view: View, clickListener: (() -> Unit)?)
        : ShuttleBusViewHolder(view, clickListener), SingleItem
    class ShuttleBusTopViewHolder(view: View, clickListener: (() -> Unit)?)
        : ShuttleBusViewHolder(view, clickListener), TopItem
    class ShuttleBusMidViewHolder(view: View, clickListener: (() -> Unit)?)
        : ShuttleBusViewHolder(view, clickListener), MidItem
    class ShuttleBusBottomViewHolder(view: View, clickListener: (() -> Unit)?)
        : ShuttleBusViewHolder(view, clickListener), BottomItem
    // endregion ShuttleBusViewHolder

    // region Item Types by position
    interface Item {

        fun getBackgroundResource(): Int?
        val layout: ViewGroup
        val showDivider: Boolean
    }

    interface SingleItem : Item {
        override fun getBackgroundResource(): Int = R.drawable.bgd_item_single
        override val showDivider: Boolean get() = false
    }

    interface TopItem : Item {
        override fun getBackgroundResource(): Int = R.drawable.bgd_item_top
        override val showDivider: Boolean get() = true
    }

    interface MidItem : Item {
        override fun getBackgroundResource(): Int = R.drawable.bgd_item_mid
        override val showDivider: Boolean get() = true
    }

    interface BottomItem : Item {
        override fun getBackgroundResource(): Int = R.drawable.bgd_item_bottom
        override val showDivider: Boolean get() = false
    }

    interface HeadingItem : Item {
        override fun getBackgroundResource(): Int? = null
        override val showDivider: Boolean get() = false
    }
    // endregion Item Types by position

    // endregion ViewHolder
}