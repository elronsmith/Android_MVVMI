package ru.elron.androidmvvmi.di

import android.app.Application
import androidx.lifecycle.ViewModel
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.elron.androidmvvmi.App
import ru.elron.libdb.TodoDatabase
import ru.elron.libdb.TodoManager
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, TodoDBModule::class])
interface AppComponent {
    val todoManager: TodoManager
}

@Module
class AppModule(private val application: Application) {
    @Singleton
    @Provides
    fun provideApplication() = application
}

@Module
class TodoDBModule {
    @Singleton
    @Provides
    fun provideTodoDatabase(application: Application) = TodoDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideTodoManager(todoDatabase: TodoDatabase) = TodoManager(todoDatabase)
}

val ViewModel.DI: AppComponent
    get() = App.DI
