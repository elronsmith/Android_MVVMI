package ru.elron.androidmvvmi

import android.app.Application
import ru.elron.androidmvvmi.di.AppComponent
import ru.elron.androidmvvmi.di.AppModule
import ru.elron.androidmvvmi.di.DaggerAppComponent
import ru.elron.androidmvvmi.di.TodoDBModule

class App : Application() {
    companion object {
        lateinit var INSTANCE: App
        lateinit var DI: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        DI = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .todoDBModule(TodoDBModule())
            .build()
    }
}
