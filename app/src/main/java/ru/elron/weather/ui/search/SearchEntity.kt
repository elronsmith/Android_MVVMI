package ru.elron.weather.ui.search

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.google.android.material.internal.TextWatcherAdapter
import ru.elron.libmvi.AEntity

class SearchEntity : AEntity {
    val city = ObservableField<String>("")
    val progressVisible = ObservableBoolean(false)
    val searchEnabled = ObservableBoolean(true)

    val cityListener = @SuppressLint("RestrictedApi")
    object : TextWatcherAdapter() {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!city.get().equals(s.toString()))
                city.set(s.toString())
        }
    }
    var cityActionListener: TextView.OnEditorActionListener? = null
    var onKeyListener: View.OnKeyListener? = null

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
