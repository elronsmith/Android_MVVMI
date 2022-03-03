package ru.elron.libmvi

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * добавляет себя в SingleObserver
 */
class SingleLiveData<T>(private val defaultValue: T? = null) : MutableLiveData<T>(defaultValue) {
    fun observe(owner: LifecycleOwner, observer: SingleObserver<T>) {
        observer.liveData = this
        super.observe(owner, observer)
    }

    fun observeForever(observer: SingleObserver<T>) {
        observer.liveData = this
        super.observeForever(observer)
    }

    internal fun setDefault(): Unit = setValue(defaultValue)
}

/**
 * когда приходит значение: удаляет его в SingleLiveData
 */
abstract class SingleObserver<T> : Observer<T> {
    internal var liveData: SingleLiveData<T>? = null

    override fun onChanged(value: T) {
        if (value == null) return
        else {
            liveData?.value = null as T
            onChangedSingle(value)
        }
    }

    fun setDefault() = liveData?.setDefault()

    fun unsubscribe() {
        if (liveData != null) {
            liveData!!.removeObserver(this)
            liveData = null
        }
    }

    abstract fun onChangedSingle(value: T)
}
