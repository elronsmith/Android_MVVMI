package ru.elron.weather.ui.search

import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import ru.elron.libmvi.BaseViewModel
import ru.elron.libnet.model.ForecastWeather5dayResponse
import ru.elron.libresources.AObservable
import ru.elron.libresources.OnItemClickViewHolderCallback
import ru.elron.libresources.RecyclerAdapter
import ru.elron.weather.extensions.hideKeyboardAndClearFocus
import ru.elron.weather.extensions.isEnter
import ru.elron.weather.extensions.toSearchItemObservable
import ru.elron.weather.manager.ForecastWeather5dayResult
import ru.elron.weather.manager.WeatherManager
import ru.elron.weather.observable.SearchItemObservable
import ru.elron.weather.observable.SearchItemViewHolder

class SearchViewModel(application: Application, stateHandle: SavedStateHandle) :
    BaseViewModel<SearchEntity, SearchState, SearchEvent>(
        application,
        stateHandle,
        "search_entity",
        SearchState.Nothing
    ), OnItemClickViewHolderCallback {

    private val manager: WeatherManager by KoinJavaComponent.inject(WeatherManager::class.java)

    val adapter = RecyclerAdapter<SearchItemObservable>()

    val isButtonsEnabled: Boolean
        get() = !entity.progressVisible.get()

    private val cityActionListener =
        TextView.OnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performRequestSearch()

                if (v is EditText)
                    v.hideKeyboardAndClearFocus()

                return@OnEditorActionListener true
            }

            false
        }

    private val onKeyListener = View.OnKeyListener { v, _, event ->
        if (event.isEnter) {
            performRequestSearch()

            if (v is EditText)
                v.hideKeyboardAndClearFocus()

            return@OnKeyListener true
        }

        false
    }

    init {
        SearchItemViewHolder.addViewHolder(adapter.holderBuilderArray, this)
    }

    override fun getNewEntity(): SearchEntity = SearchEntity()

    override fun setupEntity() {
        entity.cityActionListener = cityActionListener
        entity.onKeyListener = onKeyListener
    }

    private fun performRequestSearch() {
        val city = entity.city.get()?.trim()
        if (city.isNullOrEmpty())
            return

        if (entity.progressVisible.get())
            return

        entity.progressVisible.set(true)

        viewModelScope.launch(Dispatchers.IO) {
            val result = manager.getForecastWeather5day(city)
            when (result) {
                is ForecastWeather5dayResult.Success -> {
                    handleRequestSearch(result.response)
                }
                is ForecastWeather5dayResult.Error -> {
                    val message = result.errorResponse.message ?: ""
                    eventLiveData.postValue(SearchEvent.ShowDialogError(message))
                }
                ForecastWeather5dayResult.UnknownError -> {
                    eventLiveData.postValue(SearchEvent.ShowDialogErrorUnknown)
                }
                ForecastWeather5dayResult.ErrorInternet -> {
                    eventLiveData.postValue(SearchEvent.ShowDialogErrorInternet)
                }
            }

            withContext(Dispatchers.Main) {
                entity.progressVisible.set(false)
            }
        }
    }

    private suspend fun handleRequestSearch(response: ForecastWeather5dayResponse) {
        withContext(Dispatchers.Main) {
            adapter.observableList.clear()
            val o = response.toSearchItemObservable()
            adapter.observableList.add(o)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(v: View?, observable: AObservable, position: Int) {
        if (!isButtonsEnabled)
            return

        val cityId = (observable as SearchItemObservable).id
        eventLiveData.postValue(SearchEvent.ShowScreenWeather(cityId))
    }

    override fun getObservable(position: Int): AObservable = adapter.observableList[position]
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
