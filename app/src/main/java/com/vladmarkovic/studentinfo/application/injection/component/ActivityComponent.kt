package com.vladmarkovic.studentinfo.application.injection.component

import android.app.Activity
import com.vladmarkovic.studentinfo.application.injection.annotation.PerActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by Vlad Markovic on 2019-09-09.
 */
@PerActivity
@Subcomponent()
interface ActivityComponent : AndroidInjector<Activity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<Activity>()
}