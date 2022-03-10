package ru.elron.weather.ui.weather.forecast

import ru.elron.libmvi.IEvent

sealed class ForecastEvent : IEvent {
    object Back : ForecastEvent()
    object ErrorUnknown : ForecastEvent()
}
