package ru.elron.androidmvvmi.extensions

import ru.elron.androidmvvmi.observable.TodoItemObservable
import ru.elron.libdb.TodoEntity

fun TodoEntity.toTodoObservable(): TodoItemObservable {
    val o = TodoItemObservable.obtainObservable()

    o.id = id
    o.isChecked = checked
    o.text = text

    return o
}
