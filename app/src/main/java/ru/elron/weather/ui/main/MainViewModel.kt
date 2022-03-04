package ru.elron.weather.ui.main

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ru.elron.libmvi.BaseViewModel

class MainViewModel(application: Application, stateHandle: SavedStateHandle) :
    BaseViewModel<MainEntity, MainState, MainEvent>(
        application,
        stateHandle,
        "main_entity",
        MainState.Nothing
    ) {

    override fun getNewEntity(): MainEntity = MainEntity()

}

class MainViewModelFactory(
    private val application: Application,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle = Bundle()
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return MainViewModel(
            application,
            handle
        ) as T
    }
}
