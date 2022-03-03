package ru.elron.androidmvvmi.ui.todo

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import ru.elron.libmvi.AEntity

class TodoEntity : AEntity {
    val emptyVisible = ObservableBoolean(true)
    val progressVisible = ObservableBoolean(false)

    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<TodoEntity> {
        override fun createFromParcel(parcel: Parcel): TodoEntity {
            return TodoEntity(parcel)
        }

        override fun newArray(size: Int): Array<TodoEntity?> {
            return arrayOfNulls(size)
        }
    }

}
