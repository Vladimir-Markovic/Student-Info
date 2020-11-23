package com.vladmarkovic.studentinfo.presentation.common.screenaction

import androidx.lifecycle.Observer

/**
 * Created by Vlad Markovic on 2019-09-12.
 *
 * Helper observer for removing boilerplate code when observing screen actions.
 */
class ScreenActionObserver<A : ScreenAction>(private val handleAction: (ScreenAction) -> Unit) : Observer<A> {

    override fun onChanged(action: A?) {
        action?.let { handleAction(it) }
    }
}