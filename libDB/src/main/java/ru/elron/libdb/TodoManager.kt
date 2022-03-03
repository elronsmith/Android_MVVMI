package ru.elron.libdb

class TodoManager(database: TodoDatabase) {
    private val dao = database.todoDao()

    fun getList(): List<TodoEntity> {
        return dao.getList()
    }

    fun getByIdOrNull(id: Long): TodoEntity? {
        return dao.getTodoOrNull(id)
    }

    fun add(text: String) {
        dao.setTodo(TodoEntity(text = text))
    }

    fun update(id: Long, text: String) {
        dao.setTextById(id, text)
    }

    fun remove(id: Long) {
        dao.delete(id)
    }
}
