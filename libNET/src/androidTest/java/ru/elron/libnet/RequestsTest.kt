package ru.elron.libnet

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import retrofit2.Retrofit

@RunWith(AndroidJUnit4::class)
class RequestsTest {
    var retrofit: Retrofit = NetRepository.obtainRetrofit(
        NetRepository.MAIN_URL,
        NetRepository.obtainOkHttpClient()
    )


    @Test
    fun getForecastWeather5day_isCorrect() {
        val city = "Moscow"
        val token = NetRepository.TOKEN
        val units = "metric"
        val exclude = "minutely,hourly"

        val authRequests = NetRepository.obtainRequests(retrofit)
        val call = authRequests.getForecastWeather5day(city, token, units, exclude)

        val response = call.execute()
        val code = response.code()
        val forecastWeather5day = response.body()

        Assert.assertEquals(200, code)
        Assert.assertNotNull(forecastWeather5day)
        Assert.assertNotNull(forecastWeather5day!!.list)
        Assert.assertTrue(forecastWeather5day.list!!.isNotEmpty())
        Assert.assertNotNull(forecastWeather5day.city)
    }

    @Test
    fun getForecastWeather5day_isIncorrect() {
        val city = "Mosc"
        val token = NetRepository.TOKEN
        val units = "metric"
        val exclude = "minutely,hourly"

        val authRequests = NetRepository.obtainRequests(retrofit)
        val call = authRequests.getForecastWeather5day(city, token, units, exclude)

        val response = call.execute()
        val code = response.code()
        val forecastWeather5day = response.body()

        Assert.assertEquals(404, code)
        Assert.assertNull(forecastWeather5day)

        val errorResponseParser = NetRepository.obtainErrorResponseParser(retrofit)
        val errorResponse = errorResponseParser.parseErrorResponseOrNull(response)

        Assert.assertNotNull(errorResponse)
        Assert.assertNotNull(errorResponse!!.cod)
        Assert.assertNotNull(errorResponse.message)
    }
}
