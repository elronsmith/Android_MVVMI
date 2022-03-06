package ru.elron.weather.observable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import ru.elron.libresources.AObservable
import ru.elron.libresources.ClickableViewHolder
import ru.elron.libresources.OnItemClickViewHolderCallback
import ru.elron.libresources.ViewHolderBuilder
import ru.elron.weather.R
import ru.elron.weather.databinding.ItemSearchBinding

class SearchItemObservable : AObservable {
    var id = 0L
    var city = ""
    var temperature = ""
    var date = ""

    internal constructor(id: Int) : super(id)

    companion object {
        fun obtainObservable(): SearchItemObservable {
            return SearchItemObservable(SearchItemViewHolder.ID)
        }
    }
}

class SearchItemViewHolder(
    val binding: ItemSearchBinding,
    callback: OnItemClickViewHolderCallback
) : ClickableViewHolder(binding.root, callback) {
    companion object {
        const val ID = R.layout.item_search

        fun addViewHolder(
            builderList: SparseArrayCompat<ViewHolderBuilder>,
            callback: OnItemClickViewHolderCallback
        ) {
            builderList.put(ID, object : ViewHolderBuilder {
                override fun create(parent: ViewGroup): ru.elron.libresources.ViewHolder<*> {
                    return SearchItemViewHolder(
                        ItemSearchBinding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        ),
                        callback
                    )
                }
            })
        }
    }

    override fun update(position: Int) {
        val o = callback.getObservable(position) as SearchItemObservable

        binding.city.text = o.city
        binding.temperature.text = o.temperature
        binding.date.text = o.date

        binding.root.setOnClickListener(this)
    }
}
