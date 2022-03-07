package ru.elron.weather.repository

import ru.elron.libdb.cache.CacheDao
import ru.elron.libdb.cache.CacheEntity
import ru.elron.weather.extensions.min

class CacheDBRepository(private val cacheDao: CacheDao) {
    private val maxSize = 10
    private val maxTime = 5 * 60 * 1000L
    private val ramCache = RamCache(2, maxTime)

    fun getCityOrNull(cityId: Long): CacheEntity? {
        var entity = ramCache.getCityOrNull(cityId)
        if (entity != null && !isExpired(entity))
            return entity

        entity = cacheDao.getCityOrNull(cityId)
        if (entity != null && !isExpired(entity)) {
            ramCache.add(entity)
            return entity
        }

        return null
    }

    fun getCityFromCacheOrNull(city: String): CacheEntity? {
        var entity = ramCache.getCityFromCacheOrNull(city)
        if (entity != null)
            return entity

        entity = cacheDao.getCityByNameOrNull(city)
        if (entity != null)
            ramCache.add(entity)

        return entity
    }

    private fun isExpired(entity: CacheEntity): Boolean {
        return System.currentTimeMillis() > entity.dt + maxTime
    }

    fun add(entity: CacheEntity) {
        entity.dt = System.currentTimeMillis()
        ramCache.add(entity)
        cacheDao.setCity(entity)
        removeBySize()
    }

    private fun removeBySize() {
        val size = cacheDao.getSize()
        if (size <= maxSize)
            return

        var list = cacheDao.getDateList()
        while (list.size > maxSize) {
            val min = list.min()!!
            val entity = cacheDao.getCityByDtOrNull(min)
            if (entity != null) {
                cacheDao.delete(entity)
                ramCache.remove(entity)
                list = cacheDao.getDateList()
            } else
                return
        }
    }

    class RamCache(
        private val maxSize: Int,
        private val maxTime: Long
    ) {
        val queue = ArrayList<CacheEntity>()

        fun getCityOrNull(cityId: Long): CacheEntity? {
            queue.forEach {
                if (it.id == cityId) {
                    if (isExpired(it)) {
                        queue.remove(it)
                        return null
                    } else {
                        return it
                    }
                }
            }

            return null
        }

        fun getCityFromCacheOrNull(city: String): CacheEntity? {
            queue.forEach {
                if (it.city == city)
                    return it
            }

            return null
        }

        private fun isExpired(entity: CacheEntity): Boolean {
            return System.currentTimeMillis() > entity.dt + maxTime
        }

        fun add(entity: CacheEntity) {
            for (i in queue.lastIndex downTo 0)
                if (queue[i].id == entity.id)
                    queue.removeAt(i)

            queue.add(entity)
            clear()
        }

        fun remove(entity: CacheEntity) {
            for (item in queue)
                if (entity.id == item.id) {
                    queue.remove(item)
                    return
                }
        }

        private fun clear() {
            while (queue.size > maxSize)
                queue.removeAt(0)
        }
    }
}
