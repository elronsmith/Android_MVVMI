package ru.elron.weather.ui.weather

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import ru.elron.libmvi.AEntity

class WeatherEntity : AEntity {
    val ICON_ACTION_ADD = ru.elron.libresources.R.drawable.vd_favorite_border
    val ICON_ACTION_REMOVE = ru.elron.libresources.R.drawable.vd_favorite

    val title = ObservableField<String>("")
    val data = ObservableField<String>("")

    val addIconRes = ObservableInt(ICON_ACTION_ADD)

    val actionsEnabled = ObservableBoolean(false)

    var backListener: View.OnClickListener? = null
    var addListener: View.OnClickListener? = null
    var forecastListener: View.OnClickListener? = null

    val isFavorite: Boolean
        get() = addIconRes.get() == ICON_ACTION_REMOVE

    val isActionsEnabled: Boolean
        get() = actionsEnabled.get()

    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<WeatherEntity> {
        override fun createFromParcel(parcel: Parcel): WeatherEntity {
            return WeatherEntity(parcel)
        }

        override fun newArray(size: Int): Array<WeatherEntity?> {
            return arrayOfNulls(size)
        }
    }
}
