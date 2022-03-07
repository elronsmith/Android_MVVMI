package ru.elron.libdb.cache

import androidx.room.*

@Dao
abstract class CacheDao {
    companion object {
        const val TABLE_NAME = CacheEntity.TABLE_NAME
    }

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getList(): List<CacheEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :cityId")
    abstract fun getCityOrNull(cityId: Long): CacheEntity?

    @Query("SELECT * FROM $TABLE_NAME WHERE city LIKE :city")
    abstract fun getCityByNameOrNull(city: String): CacheEntity?

    @Query("SELECT * FROM $TABLE_NAME WHERE dt BETWEEN :start AND :end")
    abstract fun getOldCityList(start: Long, end: Long): List<CacheEntity>

    @Query("SELECT dt FROM $TABLE_NAME")
    abstract fun getDateList(): List<Long>

    @Query("SELECT * FROM $TABLE_NAME WHERE dt = :dt")
    abstract fun getCityByDtOrNull(dt: Long): CacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun setCity(entity: CacheEntity)

    @Delete
    abstract fun delete(entity: CacheEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :cityId")
    abstract fun delete(cityId: Long)

    @Query("DELETE FROM $TABLE_NAME")
    abstract fun deleteAll()

    @Query("SELECT COUNT(id) FROM $TABLE_NAME")
    abstract fun getSize(): Int
}
