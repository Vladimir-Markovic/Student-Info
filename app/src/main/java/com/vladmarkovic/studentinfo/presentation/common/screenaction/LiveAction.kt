package com.vladmarkovic.studentinfo.presentation.common.screenaction

import androidx.annotation.MainThread
import androidx.collection.ArraySet
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Vlad Markovic on 2019-09-12.
 *
 * Makes an action expired once it is "consumed".
 */
abstract class LiveAction<A: ScreenAction>: LiveData<A>() {

    private val observers = ArraySet<ObserverWrapper<in A>>()

    override fun observeForever(observer: Observer<in A>) {
        synchronized(observers) {
            val observerWrapper = ObserverWrapper(observer)
            if (!observers.contains(observerWrapper)) {
                observers.add(observerWrapper)
            }
            super.observeForever(observer)
        }
    }

    @MainThread
    override fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<in A>) {
        synchronized(observers) {
            val observerWrapper = ObserverWrapper(observer)
            if (!observers.contains(observerWrapper) && lifecycleOwner.lifecycle.currentState != DESTROYED) {
                observers.add(observerWrapper)
            }
            super.observe(lifecycleOwner, observerWrapper)
        }
    }

    @MainThread
    override fun removeObserver(observer: Observer<in A>) {
        synchronized(observers) {
            if (!observers.remove(observer)) {
                val iterator = observers.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().observer == observer) {
                        iterator.remove()
                        break
                    }
                }
            }
            super.removeObserver(observer)
        }
    }

    @MainThread
    override fun setValue(action: A?) {
        synchronized(observers) {
            observers.forEach { it.newEvent() }
            super.setValue(action)
        }
    }

    private class ObserverWrapper<A: ScreenAction>(val observer: Observer<in A>) : Observer<A> {

        // Initialise to true not to consume any actions on subscribing.
        private val processed: AtomicBoolean = AtomicBoolean(true)

        override fun onChanged(action: A?) {
            if (!processed.getAndSet(true)) {
                action?.let { observer.onChanged(it) }
            }
        }

        fun newEvent() {
            processed.set(false)
        }
    }
}