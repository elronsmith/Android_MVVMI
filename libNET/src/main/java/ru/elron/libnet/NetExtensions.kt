package ru.elron.libnet

import retrofit2.Call
import retrofit2.Response

fun <T> Call<T>.executeOrNull(): Pair<Response<T>?, Exception?> {
    return try {
        Pair(execute(), null)
    } catch (e: Exception) {
        e.printStackTrace()
        Pair(null, e)
    }
}
