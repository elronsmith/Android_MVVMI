package ru.elron.sensors.ui.main

import ru.elron.libmvi.IState

sealed class MainState : IState {
    object Nothing : MainState()
}
