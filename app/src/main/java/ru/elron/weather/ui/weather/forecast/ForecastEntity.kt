package ru.elron.weather.ui.weather.forecast

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import ru.elron.libmvi.AEntity

class ForecastEntity : AEntity {
    val title = ObservableField<String>("")
    val emptyVisible = ObservableBoolean(true)

    var backListener: View.OnClickListener? = null

    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ForecastEntity> {
        override fun createFromParcel(parcel: Parcel): ForecastEntity {
            return ForecastEntity(parcel)
        }

        override fun newArray(size: Int): Array<ForecastEntity?> {
            return arrayOfNulls(size)
        }
    }
}
