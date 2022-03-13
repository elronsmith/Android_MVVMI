package ru.elron.sensors.extensions

import android.view.View

fun View.postPeriodically(
    delayInMillis: Long,
    action: () -> Unit
) {
    val runnable: Runnable = object : Runnable {
        override fun run() {
            action.invoke()
            postDelayed(this, delayInMillis)
        }
    }
    post(runnable)
}
