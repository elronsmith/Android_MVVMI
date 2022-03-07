package ru.elron.weather.ui.search

import ru.elron.libmvi.IEvent

sealed class SearchEvent : IEvent {
    data class ShowScreenWeather(val cityId: Long) : SearchEvent()
    data class ShowDialogError(val message: String) : SearchEvent()
    object ShowDialogErrorUnknown : SearchEvent()
    object ShowDialogErrorInternet : SearchEvent()
}
