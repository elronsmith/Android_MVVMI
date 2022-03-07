package ru.elron.libnet

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetRepository {
    val MAIN_URL = "http://api.openweathermap.org/"
    val TOKEN = "3aec242b4db00a9092b74b2233a86a4d"

    fun obtainGSON(): Gson = GsonBuilder().create()

    fun obtainOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun obtainRetrofit(mainUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(mainUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun obtainErrorResponseParser(retrofit: Retrofit): ErrorResponseParser {
        return ErrorResponseParser(retrofit)
    }

    fun obtainRequests(retrofit: Retrofit): IRequests {
        return retrofit.create(IRequests::class.java)
    }
}
