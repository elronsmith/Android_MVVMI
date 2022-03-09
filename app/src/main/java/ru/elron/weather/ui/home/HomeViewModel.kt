package ru.elron.weather.ui.home

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import ru.elron.libdb.favorite.FavoriteEntity
import ru.elron.libmvi.BaseViewModel
import ru.elron.libresources.AObservable
import ru.elron.libresources.OnItemClickViewHolderCallback
import ru.elron.libresources.RecyclerAdapter
import ru.elron.weather.extensions.toSearchItemObservable
import ru.elron.weather.manager.WeatherManager
import ru.elron.weather.observable.SearchItemObservable
import ru.elron.weather.observable.SearchItemViewHolder

class HomeViewModel(application: Application, stateHandle: SavedStateHandle) :
    BaseViewModel<HomeEntity, HomeState, HomeEvent>(
        application,
        stateHandle,
        "home_entity",
        HomeState.Nothing
    ), OnItemClickViewHolderCallback {

    private val manager: WeatherManager by KoinJavaComponent.inject(WeatherManager::class.java)

    val adapter = RecyclerAdapter<SearchItemObservable>()

    private val isButtonsEnabled: Boolean
        get() = !entity.progressVisible.get()

    init {
        SearchItemViewHolder.addViewHolder(adapter.holderBuilderArray, this)
    }

    override fun getNewEntity(): HomeEntity = HomeEntity()

    fun performGetFavoriteList() {
        if (!isButtonsEnabled)
            return

        entity.progressVisible.set(true)
        viewModelScope.launch(Dispatchers.IO) {
            val favorites = manager.getFavoriteList()
            handleGetFavoriteList(favorites)
            withContext(Dispatchers.Main) {
                entity.progressVisible.set(false)
            }
        }
    }

    private suspend fun handleGetFavoriteList(list: List<FavoriteEntity>) {
        withContext(Dispatchers.Main) {
            adapter.observableList.clear()
            list.forEach {
                val o = it.toSearchItemObservable()
                adapter.observableList.add(o)
            }
            adapter.notifyDataSetChanged()
            entity.emptyVisible.set(adapter.observableList.isEmpty())
        }
    }

    override fun onItemClick(v: View?, observable: AObservable, position: Int) {
        if (!isButtonsEnabled)
            return

        val cityId = (observable as SearchItemObservable).id
        eventLiveData.postValue(HomeEvent.ShowScreenWeather(cityId))
    }

    override fun getObservable(position: Int): AObservable = adapter.observableList[position]
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
