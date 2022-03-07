package ru.elron.libdb.favorite

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = FavoriteEntity.TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "city")
    var city: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "temperature")
    var temperature: String,

    @ColumnInfo(name = "data")
    var data: String,
) {
    companion object {
        const val TABLE_NAME = "favorite"
    }
}
