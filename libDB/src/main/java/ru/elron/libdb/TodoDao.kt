package ru.elron.libdb

import androidx.room.*

@Dao
abstract class TodoDao {
    @Query("SELECT * FROM ${TodoEntity.TABLE_NAME}")
    abstract fun getList(): List<TodoEntity>

    @Query("SELECT * FROM ${TodoEntity.TABLE_NAME} WHERE id = :id")
    abstract fun getTodoOrNull(id: Long): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun setTodo(work: TodoEntity)

    @Query("UPDATE ${TodoEntity.TABLE_NAME} SET text=:text WHERE id=:id")
    abstract fun setTextById(id: Long, text: String)

    @Delete
    abstract fun delete(work: TodoEntity)

    @Query("DELETE FROM ${TodoEntity.TABLE_NAME} WHERE id = :id")
    abstract fun delete(id: Long)

    @Query("DELETE FROM ${TodoEntity.TABLE_NAME}")
    abstract fun deleteAll()

    @Query("SELECT COUNT(id) FROM ${TodoEntity.TABLE_NAME}")
    abstract fun getSize(): Int
}
