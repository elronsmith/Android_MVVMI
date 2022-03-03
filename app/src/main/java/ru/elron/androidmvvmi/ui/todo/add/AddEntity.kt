package ru.elron.androidmvvmi.ui.todo.add

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.android.material.internal.TextWatcherAdapter
import ru.elron.libmvi.AEntity

class AddEntity : AEntity {
    val text = ObservableField("")
    val textListener = @SuppressLint("RestrictedApi")
    object : TextWatcherAdapter() {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!text.get().equals(s.toString()))
                text.set(s.toString())
        }
    }

    val progressVisible = ObservableBoolean(false)

    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<AddEntity> {
        override fun createFromParcel(parcel: Parcel): AddEntity {
            return AddEntity(parcel)
        }

        override fun newArray(size: Int): Array<AddEntity?> {
            return arrayOfNulls(size)
        }
    }

}
