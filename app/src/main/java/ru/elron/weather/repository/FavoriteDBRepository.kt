package ru.elron.weather.repository

import ru.elron.libdb.favorite.FavoriteDao
import ru.elron.libdb.favorite.FavoriteEntity

class FavoriteDBRepository(private val favoriteDao: FavoriteDao) {
    fun getList(): List<FavoriteEntity> {
        return favoriteDao.getList()
    }

    fun getCityOrNull(cityId: Long): FavoriteEntity? {
        return favoriteDao.getCityOrNull(cityId)
    }

    fun isFavorite(cityId: Long): Boolean {
        return favoriteDao.getCountByCityId(cityId) > 0
    }

    fun set(entity: FavoriteEntity) {
        favoriteDao.setCity(entity)
    }

    fun remove(cityId: Long) {
        favoriteDao.delete(cityId)
    }
}
