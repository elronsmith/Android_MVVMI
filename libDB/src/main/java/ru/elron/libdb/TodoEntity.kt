package ru.elron.libdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TodoEntity.TABLE_NAME)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,

    @ColumnInfo(name = "text")
    var text: String,

    @ColumnInfo(name = "checked")
    var checked: Boolean = false,
) {
    companion object {
        const val TABLE_NAME = "todo"
    }
}
