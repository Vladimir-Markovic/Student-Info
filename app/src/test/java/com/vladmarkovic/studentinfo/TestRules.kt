package com.vladmarkovic.studentinfo

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.ViewModel
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


/**
 * Created by Vlad Markovic on 2019-09-09.
 */

/**
 * In order to test LiveData, the `InstantTaskExecutorRule` rule needs to be applied via JUnit.
 * As we are running it with Spek, the "rule" will be implemented in this way instead.
 */
fun instantTaskExecutorRuleStart() =
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })

fun instantTaskExecutorRuleFinish() = ArchTaskExecutor.getInstance().setDelegate(null)

fun setRxSchedulersMainOnTrampoline() = RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

fun setRxSchedulersIoOnTrampoline() = RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }

fun makeImmediateScheduler(): Scheduler = object : Scheduler() {
    override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit) = super.scheduleDirect(run, 0, unit)
    override fun createWorker() = ExecutorScheduler.ExecutorWorker(Executor { it.run() })
}

fun setTestRxAndLiveData(scheduler: Scheduler) {
    setRxScheduler(scheduler)
    instantTaskExecutorRuleStart()
}

fun setRxScheduler(scheduler: Scheduler) {
    RxJavaPlugins.setComputationSchedulerHandler { scheduler }
    RxJavaPlugins.setIoSchedulerHandler { scheduler }
    RxJavaPlugins.setInitIoSchedulerHandler { scheduler }
    RxJavaPlugins.setInitComputationSchedulerHandler { scheduler }
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
    RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler }
}

fun setTestRxAndLiveData() {
    setRxSchedulersMainOnTrampoline()
    setRxSchedulersIoOnTrampoline()
    instantTaskExecutorRuleStart()
}

fun resetTestRxAndLiveData() {
    resetTestRx()
    instantTaskExecutorRuleFinish()
}

fun resetTestRx() {
    RxAndroidPlugins.reset()
    RxJavaPlugins.reset()
}

fun resetTestRxAndLiveData(viewModel: ViewModel) {
    viewModel.javaClass.getDeclaredMethod("onCleared")
        .apply { isAccessible = true }
        .invoke(viewModel)

    resetTestRxAndLiveData()
}