package ru.elron.sensors.ui.sensor.orientation

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.SensorManager.SENSOR_DELAY_NORMAL
import android.hardware.SensorManager.SENSOR_STATUS_NO_CONTACT
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ru.elron.libmvi.BaseViewModel

class OrientationViewModel(application: Application, stateHandle: SavedStateHandle) :
    BaseViewModel<OrientationEntity, OrientationState, OrientationEvent>(
        application,
        stateHandle,
        "orientation_entity",
        OrientationState.Nothing
    ), SensorEventListener {

    private val sensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var orientation: Sensor? = null
    private val sb = StringBuilder()

    var timestamp = 0L
    var stateAccuracy = SENSOR_STATUS_NO_CONTACT
    val values = floatArrayOf(0f, 0f, 0f)

    override fun getNewEntity(): OrientationEntity = OrientationEntity()

    override fun onCreateView() {
        val list = sensorManager.getSensorList(Sensor.TYPE_ALL)
        Log.d("TAG", "onCreateView: count=${list.size}")
        list.forEach { sensor ->
            Log.d("TAG", "onCreateView: $sensor")
            if (sensor.type == Sensor.TYPE_ORIENTATION)
                orientation = sensor
        }
        Log.d("TAG", "onCreateView: orientation=$orientation")

    }

    fun subscribe() {
        orientation?.let {
            val success = sensorManager.registerListener(this, it, SENSOR_DELAY_NORMAL)
            Log.d("TAG", "onResume: success=$success")
            entity.emptyVisible.set(!success)
            entity.dataVisible.set(success)
        }
    }

    fun unsubscribe() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            timestamp = event.timestamp
            stateAccuracy = event.accuracy
            for (i in event.values.indices) {
                values[i] = event.values[i]
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("TAG", "onAccuracyChanged: $accuracy")
    }

    fun update() {
        sb.setLength(0)
        sb.append(timestamp).append("\n")
        sb.append(stateAccuracy).append("\n")
        for (value in values) {
            sb.append(value).append("\n")
        }

        entity.data.set(sb.toString())
    }
}

class OrientationViewModelFactory(
    private val application: Application,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle = Bundle()
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return OrientationViewModel(
            application,
            handle
        ) as T
    }
}
