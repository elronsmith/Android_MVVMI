package ru.elron.weather.manager

import ru.elron.libdb.cache.CacheEntity
import ru.elron.libnet.ErrorResponseParser
import ru.elron.libnet.IRequests
import ru.elron.libnet.NetRepository
import ru.elron.libnet.executeOrNull
import ru.elron.libnet.model.ErrorResponse
import ru.elron.libnet.model.ForecastWeather5dayResponse
import ru.elron.weather.extensions.toCacheEntity
import ru.elron.weather.extensions.toFavoriteEntity
import ru.elron.weather.extensions.toForecastWeather5dayResponse
import ru.elron.weather.repository.CacheDBRepository
import ru.elron.weather.repository.FavoriteDBRepository
import java.net.UnknownHostException

class WeatherManager(
    private val requests: IRequests,
    private val parser: ErrorResponseParser,
    private val favoriteRepository: FavoriteDBRepository,
    private val cacheRepository: CacheDBRepository
) {
    private val token = NetRepository.TOKEN
    private val units = "metric"
    private val exclude = "minutely,hourly"

    fun getForecastWeather5day(city: String): ForecastWeather5dayResult {
        val call = requests.getForecastWeather5day(city, token, units, exclude)
        val pair = call.executeOrNull()
        val response = pair.first

        if (response == null) {
            if (pair.second is UnknownHostException) {
                val entity = cacheRepository.getCityFromCacheOrNull(city)
                if (entity != null) {
                    val forecastWeather5dayResponse = entity.toForecastWeather5dayResponse()
                    return ForecastWeather5dayResult.Success(forecastWeather5dayResponse)
                } else
                    return ForecastWeather5dayResult.ErrorInternet
            } else
                return ForecastWeather5dayResult.UnknownError
        } else if (response.isSuccessful) {
            val forecastWeather5dayResponse = response.body()
            return if (forecastWeather5dayResponse == null)
                ForecastWeather5dayResult.UnknownError
            else {
                addToCache(forecastWeather5dayResponse)
                ForecastWeather5dayResult.Success(forecastWeather5dayResponse)
            }
        } else {
            val errorResponse = parser.parseErrorResponseOrNull(response)
            return if (errorResponse == null)
                ForecastWeather5dayResult.UnknownError
            else
                ForecastWeather5dayResult.Error(errorResponse)
        }
    }

    private fun addToCache(response: ForecastWeather5dayResponse) {
        val cacheEntity = response.toCacheEntity()
        if (cacheEntity.id == 0L)
            return
        if (cacheEntity.city.isBlank())
            return
        if (cacheEntity.temperature.isBlank())
            return

        cacheRepository.add(cacheEntity)

        updateFavorite(cacheEntity)
    }

    private fun updateFavorite(cacheEntity: CacheEntity) {
        if (!favoriteRepository.isFavorite(cacheEntity.id))
            return

        favoriteRepository.set(cacheEntity.toFavoriteEntity())
    }

    fun isFavorite(cityId: Long): Boolean {
        return favoriteRepository.isFavorite(cityId)
    }

    fun getWeather(cityId: Long): GetWeatherResult {
        val cacheEntity = cacheRepository.getCityOrNull(cityId)
        if (cacheEntity != null) {
            val responce = cacheEntity.toForecastWeather5dayResponse()
            return GetWeatherResult.Success(responce)
        }

        val favoriteEntity = favoriteRepository.getCityOrNull(cityId)
        if (favoriteEntity != null) {
            val responce = favoriteEntity.toForecastWeather5dayResponse()
            return GetWeatherResult.Success(responce)
        }

        return GetWeatherResult.Error
    }

    fun addFavorite(cityId: Long): AddFavoriteResult {
        val cacheEntity = cacheRepository.getCityOrNull(cityId)
        if (cacheEntity == null)
            return AddFavoriteResult.Error

        val favoriteEntity = cacheEntity.toFavoriteEntity()
        favoriteRepository.set(favoriteEntity)

        return AddFavoriteResult.Success
    }

    fun removeFavorite(cityId: Long) {
        favoriteRepository.remove(cityId)
    }
}

sealed class ForecastWeather5dayResult {
    data class Success(val response: ForecastWeather5dayResponse) : ForecastWeather5dayResult()
    data class Error(val errorResponse: ErrorResponse) : ForecastWeather5dayResult()
    object UnknownError : ForecastWeather5dayResult()
    object ErrorInternet : ForecastWeather5dayResult()
}

sealed class GetWeatherResult {
    data class Success(val response: ForecastWeather5dayResponse) : GetWeatherResult()
    object Error : GetWeatherResult()
}

sealed class AddFavoriteResult {
    object Success : AddFavoriteResult()
    object Error : AddFavoriteResult()
}
