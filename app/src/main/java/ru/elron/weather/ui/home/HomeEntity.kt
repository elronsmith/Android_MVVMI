package ru.elron.weather.ui.home

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import ru.elron.libmvi.AEntity

class HomeEntity : AEntity {
    val emptyVisible = ObservableBoolean(true)
    val progressVisible = ObservableBoolean(false)

    constructor() : super()
    constructor(parcel: Parcel) : super(parcel) {
        emptyVisible.set(parcel.readInt() == 1)
        progressVisible.set(parcel.readInt() == 1)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeInt(if (emptyVisible.get()) 1 else 0)
        parcel.writeInt(if (progressVisible.get()) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<HomeEntity> {
        override fun createFromParcel(parcel: Parcel): HomeEntity {
            return HomeEntity(parcel)
        }

        override fun newArray(size: Int): Array<HomeEntity?> {
            return arrayOfNulls(size)
        }
    }
}
