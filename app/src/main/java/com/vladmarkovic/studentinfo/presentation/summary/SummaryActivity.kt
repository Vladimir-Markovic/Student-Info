package com.vladmarkovic.studentinfo.presentation.summary

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.vladmarkovic.studentinfo.R
import com.vladmarkovic.studentinfo.presentation.common.NonNullObserver
import com.vladmarkovic.studentinfo.presentation.common.ViewModelFactory
import com.vladmarkovic.studentinfo.presentation.common.screenaction.ScreenActionObserver
import com.vladmarkovic.studentinfo.presentation.summary.SummaryViewModel.DisplayAction
import com.vladmarkovic.studentinfo.presentation.summary.SummaryViewModel.DisplayAction.ShowError
import com.vladmarkovic.studentinfo.presentation.summary.model.SummaryItem.ViewType.*
import com.vladmarkovic.studentinfo.presentation.util.toast
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_summary.*
import javax.inject.Inject

/**
 * Created by Vlad Markovic on 2019-09-09.
 *
 * Activity as a view is completely "dumb".
 */
class SummaryActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<SummaryViewModel>
    private val viewModel: SummaryViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SummaryViewModel::class.java)
    }

    private val summaryAdapter by lazy { SummaryAdapter() }

    // region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        summaryItemsList.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        summaryItemsList.adapter = summaryAdapter

        viewModel.studentName().observe(this, NonNullObserver { studentName ->
            personalGreeting.text = String.format(getString(R.string.greetings_message_format), studentName)
        })

        viewModel.currentWeek().observe(this, NonNullObserver {
            dateAndWeek.text = it
        })

        viewModel.summaryData().observe(this, NonNullObserver { summaryItems ->
            summaryAdapter.setItems(summaryItems)
        })

        viewModel.displayActions.observe(this, ScreenActionObserver { action ->
            when (action) {
                is DisplayAction -> handleDisplayActions(action)
            }
        })

        summaryAdapter.classItemClickListener = { viewModel.adjustSize(CLASS_MID, CLASS_BOTTOM) }
        summaryAdapter.carParkItemClickListener = { viewModel.adjustSize(PARK_MID, PARK_BOTTOM) }
        summaryAdapter.shuttleBusItemClickListener = { viewModel.adjustSize(BUS_MID, BUS_BOTTOM) }
    }

    override fun onStart() {
        super.onStart()
        viewModel.refreshData()
    }
    // endregion Lifecycle

    // region Screen Actions
    private fun handleDisplayActions(display: DisplayAction) {
        when (display) {
            is ShowError -> toast(this, display.error.message(this))
        }
    }
    // endregion Screen Actions
}
