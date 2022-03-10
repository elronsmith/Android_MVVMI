package ru.elron.weather.extensions

import com.google.gson.Gson
import ru.elron.libdb.cache.CacheEntity
import ru.elron.libdb.favorite.FavoriteEntity
import ru.elron.libnet.model.ForecastWeather5dayResponse
import ru.elron.weather.di.getKoinInstance
import ru.elron.weather.extensions.ConstExtensions.stringBuilder
import ru.elron.weather.observable.ForecastItemObservable
import ru.elron.weather.observable.SearchItemObservable

object ConstExtensions {
    val stringBuilder = StringBuilder()
}

val ForecastWeather5dayResponse.cityId: Long
    get() = city?.id ?: 0L

val ForecastWeather5dayResponse.cityName: String
    get() = city?.name ?: ""

fun ForecastWeather5dayResponse.getLastTemperatureOrNull(): Double? {
    if (list.isNullOrEmpty())
        return null

    return list!![0].main?.temp
}

fun ForecastWeather5dayResponse.getTemperature(): String {
    if (list.isNullOrEmpty())
        return ""

    val temp = list!![0].main?.temp
    return temp?.toInt()?.toString() ?: ""
}

fun ForecastWeather5dayResponse.getLastDateOrNull(): String? {
    if (list.isNullOrEmpty())
        return null

    return list!![0].dt_txt
}

fun ForecastWeather5dayResponse.toSearchItemObservable(): SearchItemObservable {
    val o = SearchItemObservable.obtainObservable()

    o.id = cityId
    o.city = cityName.ifBlank { "?" }
    val temp = getLastTemperatureOrNull()
    o.temperature = temp?.toInt()?.toString() ?: "?"
    o.date = getLastDateOrNull() ?: "?"
    return o
}

fun ForecastWeather5dayResponse.toStringJson(): String {
    val gson: Gson = getKoinInstance()
    return gson.toJson(this)
}

fun ForecastWeather5dayResponse.toCacheEntity(): CacheEntity {
    val json = this.toStringJson()
    val id = this.cityId
    val city = this.cityName
    val date = this.getLastDateOrNull() ?: ""
    val temperature = this.getLastTemperatureOrNull()

    return CacheEntity(
        id,
        city,
        date,
        0L,
        temperature?.toInt()?.toString() ?: "",
        json
    )
}

fun CacheEntity.toForecastWeather5dayResponse(): ForecastWeather5dayResponse {
    val gson: Gson = getKoinInstance()
    return gson.fromJson(data, ForecastWeather5dayResponse::class.java)
}

fun CacheEntity.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(id, city, date, temperature, data)
}

fun FavoriteEntity.toForecastWeather5dayResponse(): ForecastWeather5dayResponse {
    val gson: Gson = getKoinInstance()
    return gson.fromJson(data, ForecastWeather5dayResponse::class.java)
}

fun FavoriteEntity.toSearchItemObservable(): SearchItemObservable {
    val o = SearchItemObservable.obtainObservable()
    o.id = id
    o.city = city
    o.temperature = temperature
    o.date = date

    return o
}

fun ForecastWeather5dayResponse.WeatherItem.toForecastItemObservable(): ForecastItemObservable {
    val o = ForecastItemObservable.obtainObservable()
    stringBuilder.setLength(0)

    val date = dt_txt ?: "?"
    val temp = main?.temp?.toInt() ?: 99
    val pressure = main?.pressure ?: '?'
    val humidity = main?.humidity ?: '?'
    val speed = wind?.speed?.toInt() ?: 0

    with(stringBuilder) {
        append(date)
        append("\n")
        append("Temperature: $temp°")
        append("\n")
        append("Pressure: $pressure")
        append("\n")
        append("Humidity: $humidity")
        append("\n")
        append("Wind speed: $speed")
    }

    o.info = stringBuilder.toString()

    return o
}
