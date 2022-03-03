package ru.elron.androidmvvmi.di

import android.app.Application
import org.koin.dsl.module
import ru.elron.libdb.TodoDatabase
import ru.elron.libdb.TodoManager

val appModule = module {
    single<TodoDatabase> { TodoDatabase.getInstance(get<Application>()) }
    single<TodoManager> { TodoManager(get()) }
}
