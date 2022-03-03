package ru.elron.androidmvvmi.extensions

import ru.elron.androidmvvmi.observable.TodoObservable
import ru.elron.libdb.TodoEntity

fun TodoEntity.toTodoObservable(): TodoObservable {
    val o = TodoObservable.obtainObservable()

    o.id = id
    o.isChecked = checked
    o.text = text

    return o
}
