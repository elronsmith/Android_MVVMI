package ru.elron.sensors.ui.home

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import ru.elron.libmvi.BaseViewModel

class HomeViewModel(application: Application, stateHandle: SavedStateHandle) :
    BaseViewModel<HomeEntity, HomeState, HomeEvent>(
        application,
        stateHandle,
        "home_entity",
        HomeState.Nothing
    ) {

    override fun getNewEntity(): HomeEntity = HomeEntity()

}

class HomeViewModelFactory(
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
        return HomeViewModel(
            application,
            handle
        ) as T
    }
}
