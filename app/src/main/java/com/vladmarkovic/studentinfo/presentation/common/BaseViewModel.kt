package com.vladmarkovic.studentinfo.presentation.common

import androidx.lifecycle.ViewModel
import com.vladmarkovic.studentinfo.presentation.common.screenaction.Display
import com.vladmarkovic.studentinfo.presentation.common.screenaction.LiveAction
import com.vladmarkovic.studentinfo.presentation.common.screenaction.MutableLiveAction
import com.vladmarkovic.studentinfo.presentation.common.screenaction.Navigation

/**
 * Created by Vlad Markovic on 2019-09-12.
 *
 * Extend for removing the need to write the below boilerplate code in a ViewModel
 * in which we want to use the screen-actions pattern.
 *
 * Pattern makes it really easy and really readable to use - in an extending ViewModel we
 * define our navigation and display actions (using sealed class) and then simply call
 * `navigateTo(Home) or display(ErrorMessage("Error occurred!"))
 * In Activity or Fragment we access `navigationActions` or `displayActions` to observe them.
 */
abstract class BaseViewModel : ViewModel() {

    private val mutableNavigationActions = MutableLiveAction<Navigation.Action>()
    private val mutableDisplayActions = MutableLiveAction<Display.Action>()

    val navigationActions: LiveAction<Navigation.Action> = mutableNavigationActions
    val displayActions: LiveAction<Display.Action> = mutableDisplayActions

    protected open fun navigateTo(action: Navigation.Action) {
        mutableNavigationActions.value = action
    }

    protected open fun display(action: Display.Action) {
        mutableDisplayActions.value = action
    }
}