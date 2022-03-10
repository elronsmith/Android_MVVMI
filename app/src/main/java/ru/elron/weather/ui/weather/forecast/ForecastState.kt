package ru.elron.weather.ui.weather.forecast

import ru.elron.libmvi.IState

sealed class ForecastState : IState {
    object Nothing : ForecastState()
}
