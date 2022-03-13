package ru.elron.sensors.ui.sensor.orientation

import ru.elron.libmvi.IState

sealed class OrientationState : IState {
    object Nothing : OrientationState()
}
