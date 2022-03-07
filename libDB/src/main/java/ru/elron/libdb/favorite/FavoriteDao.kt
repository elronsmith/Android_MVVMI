package ru.elron.libdb.favorite

import androidx.room.*

@Dao
abstract class FavoriteDao {
    companion object {
        const val TABLE_NAME = FavoriteEntity.TABLE_NAME
    }

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getList(): List<FavoriteEntity>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :cityId")
    abstract fun getCityOrNull(cityId: Long): FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun setCity(entity: FavoriteEntity)

    @Delete
    abstract fun delete(work: FavoriteEntity)

    @Query("DELETE FROM $TABLE_NAME WHERE id = :cityId")
    abstract fun delete(cityId: Long)

    @Query("DELETE FROM $TABLE_NAME")
    abstract fun deleteAll()

    @Query("SELECT COUNT(id) FROM $TABLE_NAME")
    abstract fun getSize(): Int

    @Query("SELECT COUNT(id) FROM $TABLE_NAME WHERE id = :cityId")
    abstract fun getCountByCityId(cityId: Long): Int
}
