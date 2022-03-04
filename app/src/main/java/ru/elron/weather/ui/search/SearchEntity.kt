package ru.elron.weather.ui.search

import android.os.Parcel
import android.os.Parcelable
import ru.elron.libmvi.AEntity

class SearchEntity : AEntity {
    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SearchEntity> {
        override fun createFromParcel(parcel: Parcel): SearchEntity {
            return SearchEntity(parcel)
        }

        override fun newArray(size: Int): Array<SearchEntity?> {
            return arrayOfNulls(size)
        }
    }
}
