package ru.elron.weather.observable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import ru.elron.libresources.AObservable
import ru.elron.libresources.ClickableViewHolder
import ru.elron.libresources.OnItemClickViewHolderCallback
import ru.elron.libresources.ViewHolderBuilder
import ru.elron.weather.R
import ru.elron.weather.databinding.ItemForecastBinding

class ForecastItemObservable : AObservable {
    var id = 0L
    var info = ""

    internal constructor(layoutId: Int) : super(layoutId)

    companion object {
        fun obtainObservable(): ForecastItemObservable {
            return ForecastItemObservable(ForecastItemViewHolder.ID)
        }
    }
}

class ForecastItemViewHolder(
    val binding: ItemForecastBinding,
    callback: OnItemClickViewHolderCallback
) : ClickableViewHolder(binding.root, callback) {
    companion object {
        const val ID = R.layout.item_forecast

        fun addViewHolder(
            builderList: SparseArrayCompat<ViewHolderBuilder>,
            callback: OnItemClickViewHolderCallback
        ) {
            builderList.put(ID, object : ViewHolderBuilder {
                override fun create(parent: ViewGroup): ru.elron.libresources.ViewHolder<*> {
                    return ForecastItemViewHolder(
                        ItemForecastBinding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        ),
                        callback
                    )
                }
            })
        }
    }

    override fun update(position: Int) {
        val o = callback.getObservable(position) as ForecastItemObservable
        binding.infoTextView.text = o.info
    }
}
