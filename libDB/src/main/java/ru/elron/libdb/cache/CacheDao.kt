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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun setCity(entity: CacheEntity)

    @Delete
    abstract fun delete(work: CacheEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :cityId")
    abstract fun delete(cityId: Long)

    @Query("DELETE FROM $TABLE_NAME")
    abstract fun deleteAll()

    @Query("SELECT COUNT(id) FROM $TABLE_NAME")
    abstract fun getSize(): Int
}
