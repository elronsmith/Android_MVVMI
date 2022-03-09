package ru.elron.weather.ui.home

import ru.elron.libmvi.IEvent

sealed class HomeEvent : IEvent {
    data class ShowScreenWeather(val cityId: Long) : HomeEvent()
}
