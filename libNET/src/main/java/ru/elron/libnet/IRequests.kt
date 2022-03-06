package ru.elron.libnet

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.elron.libnet.model.ForecastWeather5dayResponse

interface IRequests {
    @GET("data/2.5/forecast")
    fun getForecastWeather5day(
        @Query("q") city: String,
        @Query("appid") token: String,
        @Query("units") units: String,
        @Query("exclude") exclude: String,
    ): Call<ForecastWeather5dayResponse?>
}
