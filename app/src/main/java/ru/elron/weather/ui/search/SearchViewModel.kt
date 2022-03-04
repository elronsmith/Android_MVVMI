package ru.elron.weather.ui.search

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ru.elron.libmvi.BaseViewModel

class SearchViewModel(application: Application, stateHandle: SavedStateHandle) :
    BaseViewModel<SearchEntity, SearchState, SearchEvent>(
        application,
        stateHandle,
        "search_entity",
        SearchState.Nothing
    ) {

    override fun getNewEntity(): SearchEntity = SearchEntity()

}

class SearchViewModelFactory(
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
        return SearchViewModel(
            application,
            handle
        ) as T
    }
}
