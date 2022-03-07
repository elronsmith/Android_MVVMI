package ru.elron.weather.ui.weather

import ru.elron.libmvi.IState

sealed class WeatherState : IState {
    object Nothing : WeatherState()
}
