package ru.elron.androidmvvmi.ui.todo

import ru.elron.libmvi.IState

sealed class TodoState : IState {
    object Nothing : TodoState()
}
