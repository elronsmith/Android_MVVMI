package ru.elron.weather.manager

import ru.elron.libnet.ErrorResponseParser
import ru.elron.libnet.IRequests
import ru.elron.libnet.NetRepository
import ru.elron.libnet.executeOrNull
import ru.elron.libnet.model.ErrorResponse
import ru.elron.libnet.model.ForecastWeather5dayResponse

class WeatherManager(
    private val requests: IRequests,
    private val parser: ErrorResponseParser
) {
    private val token = NetRepository.TOKEN
    private val units = "metric"
    private val exclude = "minutely,hourly"

    fun getForecastWeather5day(city: String): ForecastWeather5dayResult {
        val call = requests.getForecastWeather5day(city, token, units, exclude)
        val response = call.executeOrNull()

        if (response == null) {
            return ForecastWeather5dayResult.UnknownError
        } else if (response.isSuccessful) {
            val forecastWeather5dayResponse = response.body()
            return if (forecastWeather5dayResponse == null)
                ForecastWeather5dayResult.UnknownError
            else
                ForecastWeather5dayResult.Success(forecastWeather5dayResponse)
        } else {
            val errorResponse = parser.parseErrorResponseOrNull(response)
            return if (errorResponse == null)
                ForecastWeather5dayResult.UnknownError
            else
                ForecastWeather5dayResult.Error(errorResponse)
        }
    }
}

sealed class ForecastWeather5dayResult {
    data class Success(val response: ForecastWeather5dayResponse) : ForecastWeather5dayResult()
    data class Error(val errorResponse: ErrorResponse) : ForecastWeather5dayResult()
    object UnknownError : ForecastWeather5dayResult()
}
