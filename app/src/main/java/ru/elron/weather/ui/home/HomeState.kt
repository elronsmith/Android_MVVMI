package ru.elron.weather.ui.home

import ru.elron.libmvi.IState

sealed class HomeState : IState {
    object Nothing : HomeState()
}
