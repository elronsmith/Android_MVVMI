package ru.elron.libnet

import retrofit2.Call
import retrofit2.Response

fun <T> Call<T>.executeOrNull(): Response<T>? {
    return try {
        execute()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
