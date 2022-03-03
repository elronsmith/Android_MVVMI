package ru.elron.androidmvvmi.ui.todo

import ru.elron.libmvi.IEvent

sealed class TodoEvent : IEvent {
    data class ShowScreenEdit(val id: Long) : TodoEvent()
    data class Remove(val index: Int, val text: String) : TodoEvent()
}
