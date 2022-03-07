package ru.elron.libdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.elron.libdb.cache.CacheDao
import ru.elron.libdb.cache.CacheEntity
import ru.elron.libdb.favorite.FavoriteDao
import ru.elron.libdb.favorite.FavoriteEntity

@Database(entities = [FavoriteEntity::class, CacheEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cacheDao(): CacheDao

    companion object {
        private val DATABASE_NAME = "weather.db"
        fun getInstance(context: Context): WeatherDatabase {
            return Room.databaseBuilder(context, WeatherDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}
