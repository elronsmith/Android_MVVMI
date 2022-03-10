package ru.elron.weather.ui.weather.forecast

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
import ru.elron.libmvi.BaseViewModel
import ru.elron.libnet.model.ForecastWeather5dayResponse
import ru.elron.libresources.AObservable
import ru.elron.libresources.OnItemClickViewHolderCallback
import ru.elron.libresources.RecyclerAdapter
import ru.elron.weather.extensions.cityName
import ru.elron.weather.extensions.toForecastItemObservable
import ru.elron.weather.manager.GetWeatherResult
import ru.elron.weather.manager.WeatherManager
import ru.elron.weather.observable.ForecastItemObservable
import ru.elron.weather.observable.ForecastItemViewHolder

class ForecastViewModel(application: Application, stateHandle: SavedStateHandle, val cityId: Long) :
    BaseViewModel<ForecastEntity, ForecastState, ForecastEvent>(
        application,
        stateHandle,
        "forecast_weather_entity",
        ForecastState.Nothing
    ), OnItemClickViewHolderCallback {

    private val manager: WeatherManager by KoinJavaComponent.inject(WeatherManager::class.java)

    val adapter = RecyclerAdapter<ForecastItemObservable>()

    private val backListener = View.OnClickListener {
        eventLiveData.postValue(ForecastEvent.Back)
    }

    init {
        ForecastItemViewHolder.addViewHolder(adapter.holderBuilderArray, this)
    }

    override fun getNewEntity(): ForecastEntity =ForecastEntity()

    override fun setupEntity() {
        entity.backListener = backListener
    }

    override fun onCreateView() {
        performGetCity()
    }

    private fun performGetCity() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = manager.getWeather(cityId)
            when (result) {
                is GetWeatherResult.Success -> handleGetCity(result.response)
                GetWeatherResult.Error -> {
                    eventLiveData.postValue(ForecastEvent.ErrorUnknown)
                }
            }
        }
    }

    private suspend fun handleGetCity(response: ForecastWeather5dayResponse) {
        withContext(Dispatchers.Main) {
            entity.title.set(response.cityName)

            adapter.observableList.clear()

            response.list?.forEach {
                val o = it.toForecastItemObservable()
                adapter.observableList.add(o)
            }

            adapter.notifyDataSetChanged()

            entity.emptyVisible.set(adapter.observableList.isEmpty())
        }
    }

    override fun onItemClick(v: View?, observable: AObservable, position: Int) { }

    override fun getObservable(position: Int): AObservable = adapter.observableList[position]
}

class ForecastViewModelFactory(
    private val application: Application,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle = Bundle(),
    val cityId: Long
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return ForecastViewModel(
            application,
            handle,
            cityId
        ) as T
    }
}
