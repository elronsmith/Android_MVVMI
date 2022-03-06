package ru.elron.weather.di

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.elron.libnet.ErrorResponseParser
import ru.elron.libnet.IRequests
import ru.elron.libnet.NetRepository
import ru.elron.weather.manager.WeatherManager

val appModule = module {
    single<OkHttpClient> { NetRepository.obtainOkHttpClient() }
    single<Retrofit> { NetRepository.obtainRetrofit(NetRepository.MAIN_URL, get()) }
    single<ErrorResponseParser> { NetRepository.obtainErrorResponseParser(get()) }
    single<IRequests> { NetRepository.obtainRequests(get()) }
    single<WeatherManager> { WeatherManager(get(), get()) }
}
