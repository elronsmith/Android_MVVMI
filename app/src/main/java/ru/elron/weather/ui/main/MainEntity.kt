package ru.elron.weather.ui.main

import android.os.Parcel
import android.os.Parcelable
import ru.elron.libmvi.AEntity

class MainEntity : AEntity {
    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MainEntity> {
        override fun createFromParcel(parcel: Parcel): MainEntity {
            return MainEntity(parcel)
        }

        override fun newArray(size: Int): Array<MainEntity?> {
            return arrayOfNulls(size)
        }
    }
}
