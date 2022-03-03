package ru.elron.androidmvvmi.ui.todo.add

import ru.elron.libmvi.IState

sealed class AddState : IState {
    object Nothing : AddState()
}
