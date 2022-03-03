package ru.elron.libmvi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SubscriberLiveData<T>(defaultValue: T? = null) : MutableLiveData<T>(defaultValue) {
    fun observe(owner: LifecycleOwner, observer: SubscriberObserver<T>) {
        observer.liveData = this
        super.observe(owner, observer)
    }

    fun observeForever(observer: SubscriberObserver<T>) {
        observer.liveData = this
        super.observeForever(observer)
    }
}

abstract class SubscriberObserver<T> : Observer<T> {
    internal var liveData: MutableLiveData<T>? = null

    fun unsubscribe() {
        if (liveData != null) {
            liveData!!.removeObserver(this)
            liveData = null
        }
    }
}
