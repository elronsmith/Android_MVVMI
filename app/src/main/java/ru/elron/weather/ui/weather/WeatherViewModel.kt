package ru.elron.weather.ui.weather

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
import ru.elron.weather.extensions.cityName
import ru.elron.weather.extensions.getTemperature
import ru.elron.weather.manager.AddFavoriteResult
import ru.elron.weather.manager.GetWeatherResult
import ru.elron.weather.manager.WeatherManager

class WeatherViewModel(application: Application, stateHandle: SavedStateHandle, val cityId: Long) :
    BaseViewModel<WeatherEntity, WeatherState, WeatherEvent>(
        application,
        stateHandle,
        "weather_entity",
        WeatherState.Nothing
    ) {

    private val manager: WeatherManager by KoinJavaComponent.inject(WeatherManager::class.java)

    private val backListener = View.OnClickListener {
        eventLiveData.postValue(WeatherEvent.Back)
    }

    private val addListener = View.OnClickListener {
        if (entity.isFavorite)
            performRemoveFavorite()
        else
            performAddFavorite()
    }

    override fun getNewEntity(): WeatherEntity = WeatherEntity()

    override fun setupEntity() {
        entity.addListener = addListener
        entity.backListener = backListener
    }

    override fun onCreateView() {
        performGetCity()
    }

    private fun performGetCity() {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = manager.isFavorite(cityId)
            handleFavorite(isFavorite)

            val result = manager.getWeather(cityId)
            when (result) {
                is GetWeatherResult.Success -> handleGetCity(result.response)
                GetWeatherResult.Error -> {
                    eventLiveData.postValue(WeatherEvent.ErrorUnknown)
                }
            }

            withContext(Dispatchers.Main) {
                entity.actionsEnabled.set(true)
            }
        }
    }

    private suspend fun handleGetCity(response: ForecastWeather5dayResponse) {
        val cityName = response.cityName
        val temperature = response.getTemperature()
        val data = "Temperature: $temperature"

        withContext(Dispatchers.Main) {
            entity.title.set(cityName)
            entity.data.set(data)
        }
    }

    private suspend fun handleFavorite(isFavorite: Boolean) {
        withContext(Dispatchers.Main) {
            entity.addIconRes.set(
                if (isFavorite) entity.ICON_ACTION_REMOVE
                else entity.ICON_ACTION_ADD
            )
        }
    }

    private fun performAddFavorite() {
        if (!entity.isActionsEnabled)
            return

        entity.actionsEnabled.set(false)

        viewModelScope.launch(Dispatchers.IO) {
            val result = manager.addFavorite(cityId)
            when (result) {
                AddFavoriteResult.Success -> {
                    eventLiveData.postValue(WeatherEvent.ShowDialogAddFavoriteSuccess)
                }
                AddFavoriteResult.Error -> {
                    eventLiveData.postValue(WeatherEvent.ShowDialogAddFavoriteError)
                }
            }

            handleFavorite(true)

            withContext(Dispatchers.Main) {
                entity.actionsEnabled.set(true)
            }
        }
    }

    private fun performRemoveFavorite() {
        if (!entity.isActionsEnabled)
            return

        entity.actionsEnabled.set(false)

        viewModelScope.launch(Dispatchers.IO) {
            manager.removeFavorite(cityId)
            eventLiveData.postValue(WeatherEvent.ShowDialogRemoveFavoriteSuccess)

            handleFavorite(false)

            withContext(Dispatchers.Main) {
                entity.actionsEnabled.set(true)
            }
        }
    }

}

class WeatherViewModelFactory(
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
        return WeatherViewModel(
            application,
            handle,
            cityId
        ) as T
    }
}
