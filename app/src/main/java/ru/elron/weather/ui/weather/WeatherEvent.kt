package ru.elron.weather.ui.weather

import ru.elron.libmvi.IEvent

sealed class WeatherEvent : IEvent {
    object Back : WeatherEvent()
    object ErrorUnknown : WeatherEvent()
    object ShowDialogAddFavoriteSuccess : WeatherEvent()
    object ShowDialogAddFavoriteError : WeatherEvent()
    object ShowDialogRemoveFavoriteSuccess : WeatherEvent()
}
