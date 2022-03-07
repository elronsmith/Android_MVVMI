package ru.elron.weather.extensions

fun List<Long>.min(): Long? {
    if (size == 0)
        return null
    var m = Long.MAX_VALUE
    this.forEach {
        if (it < m)
            m = it
    }
    return m
}
