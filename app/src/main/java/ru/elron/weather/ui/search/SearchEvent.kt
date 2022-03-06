package ru.elron.weather.ui.search

import ru.elron.libmvi.IEvent

sealed class SearchEvent : IEvent {
    data class ShowDialogError(val message: String) : SearchEvent()
    object ShowDialogErrorUnknown : SearchEvent()
}
