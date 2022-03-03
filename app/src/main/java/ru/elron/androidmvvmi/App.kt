package ru.elron.androidmvvmi

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.elron.androidmvvmi.di.appModule

class App : Application() {
    companion object {
        lateinit var INSTANCE: App
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}
