package ru.elron.weather.ui.home

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import ru.elron.libmvi.AEntity

class HomeEntity : AEntity {
    val emptyVisible = ObservableBoolean(true)
    val progressVisible = ObservableBoolean(false)

    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

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
