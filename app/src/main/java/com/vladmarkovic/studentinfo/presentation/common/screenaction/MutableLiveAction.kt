package com.vladmarkovic.studentinfo.presentation.common.screenaction

/**
 * Created by Vlad Markovic on 2019-09-12.
 */
class MutableLiveAction<A: ScreenAction>: LiveAction<A>() {

    public override fun setValue(action: A?) = super.setValue(action)
}