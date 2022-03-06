package ru.elron.weather.extensions

import ru.elron.libnet.model.ForecastWeather5dayResponse
import ru.elron.weather.observable.SearchItemObservable

fun ForecastWeather5dayResponse.getLastTemperatureOrNull(): Double? {
    if (list.isNullOrEmpty())
        return null

    return list!![0].main?.temp
}

fun ForecastWeather5dayResponse.getLastDateOrNull(): String? {
    if (list.isNullOrEmpty())
        return null

    return list!![0].dt_txt
}

fun ForecastWeather5dayResponse.toSearchItemObservable(): SearchItemObservable {
    val o = SearchItemObservable.obtainObservable()

    o.id = city?.id ?: 0L
    o.city = city?.name ?: "?"
    val temp = getLastTemperatureOrNull()
    o.temperature = if (temp != null) temp.toInt().toString() else "?"
    o.date = getLastDateOrNull() ?: "?"
    return o
}
