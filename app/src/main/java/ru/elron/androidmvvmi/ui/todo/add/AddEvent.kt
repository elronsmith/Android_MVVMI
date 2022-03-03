package ru.elron.androidmvvmi.ui.todo.add

import ru.elron.libmvi.IEvent

sealed class AddEvent : IEvent {
    object Back : AddEvent()
}
