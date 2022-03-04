package ru.elron.libmvi

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer

/**
 * Абстрактный базовый диалог фрагмент для связи с BaseViewModel
 */
abstract class BaseDialogFragment<ENTITY : AEntity, STATE : IState, EVENT : IEvent> :
    DialogFragment() {

    protected val stateObserver = Observer<STATE> { state ->
        onState(state)
    }
    protected val eventObserver: SingleObserver<EVENT?> = object : SingleObserver<EVENT?>() {
        override fun onChangedSingle(value: EVENT?) {
            onEvent(value!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBaseViewModel().setupEntity()
        getBaseViewModel().onCreateView()
    }

    override fun onStart() {
        super.onStart()
        getBaseViewModel().stateLiveData.observe(this, stateObserver)
        getBaseViewModel().eventLiveData.observe(this, eventObserver)
    }

    override fun onStop() {
        super.onStop()
        getBaseViewModel().stateLiveData.removeObserver(stateObserver)
        getBaseViewModel().eventLiveData.removeObserver(eventObserver)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getBaseViewModel().saveCurrentState()
    }

    /**
     * Вызывается когда изменилось состояние
     */
    open fun onState(state: STATE) {}

    /**
     * Вызывается когда пришло событие, например открыть экран
     */
    open fun onEvent(event: EVENT) {}

    abstract fun getBaseViewModel(): BaseViewModel<ENTITY, STATE, EVENT>
}
