package ru.elron.weather.di

import android.app.Application
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.elron.libdb.WeatherDatabase
import ru.elron.libdb.cache.CacheDao
import ru.elron.libdb.favorite.FavoriteDao
import ru.elron.libnet.ErrorResponseParser
import ru.elron.libnet.IRequests
import ru.elron.libnet.NetRepository
import ru.elron.weather.manager.WeatherManager
import ru.elron.weather.repository.CacheDBRepository
import ru.elron.weather.repository.FavoriteDBRepository

val appModule = module {
    single<WeatherDatabase> { WeatherDatabase.getInstance(get<Application>()) }
    single<CacheDao> { get<WeatherDatabase>().cacheDao() }
    single<FavoriteDao> { get<WeatherDatabase>().favoriteDao() }
    single<CacheDBRepository> { CacheDBRepository(get()) }
    single<FavoriteDBRepository> { FavoriteDBRepository(get()) }

    single<Gson> { NetRepository.obtainGSON() }
    single<OkHttpClient> { NetRepository.obtainOkHttpClient() }
    single<Retrofit> { NetRepository.obtainRetrofit(NetRepository.MAIN_URL, get()) }
    single<ErrorResponseParser> { NetRepository.obtainErrorResponseParser(get()) }
    single<IRequests> { NetRepository.obtainRequests(get()) }

    single<WeatherManager> { WeatherManager(get(), get(), get(), get()) }
}

inline fun <reified T> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}
