package ru.elron.sensors.ui.sensor.orientation

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import ru.elron.libmvi.AEntity

class OrientationEntity : AEntity {
    val data = ObservableField<String>("")

    val dataVisible = ObservableBoolean(false)
    val emptyVisible = ObservableBoolean(true)

    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OrientationEntity> {
        override fun createFromParcel(parcel: Parcel): OrientationEntity {
            return OrientationEntity(parcel)
        }

        override fun newArray(size: Int): Array<OrientationEntity?> {
            return arrayOfNulls(size)
        }
    }
}
