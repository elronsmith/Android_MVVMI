package ru.elron.weather.ui.search

import ru.elron.libmvi.IState

sealed class SearchState : IState {
    object Nothing : SearchState()
}
