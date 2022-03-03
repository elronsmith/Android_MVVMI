package ru.elron.androidmvvmi.observable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import ru.elron.androidmvvmi.R
import ru.elron.androidmvvmi.databinding.ItemTodoBinding
import ru.elron.libresources.AObservable
import ru.elron.libresources.ClickableViewHolder
import ru.elron.libresources.OnItemClickViewHolderCallback
import ru.elron.libresources.ViewHolderBuilder

class TodoObservable : AObservable {
    var id = 0L
    var isChecked = false
    var text = ""

    internal constructor(id: Int) : super(id)

    companion object {
        fun obtainObservable(): TodoObservable {
            return TodoObservable(KeylayoutItemViewHolder.ID)
        }
    }

}

class KeylayoutItemViewHolder(
    val binding: ItemTodoBinding,
    callback: OnItemClickViewHolderCallback
) : ClickableViewHolder(binding.root, callback) {
    companion object {
        const val ID = R.layout.item_todo

        fun addViewHolder(
            builderList: SparseArrayCompat<ViewHolderBuilder>,
            callback: OnItemClickViewHolderCallback
        ) {
            builderList.put(ID, object : ViewHolderBuilder {
                override fun create(parent: ViewGroup): ru.elron.libresources.ViewHolder<*> {
                    return KeylayoutItemViewHolder(
                        ItemTodoBinding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        ),
                        callback
                    )
                }
            })
        }
    }

    override fun update(position: Int) {
        val o = callback.getObservable(position) as TodoObservable

        binding.text.text = o.text
        binding.checked.isChecked = o.isChecked

        binding.text.setOnClickListener(this)
        binding.remove.setOnClickListener(this)
        binding.checked.setOnClickListener(this)
    }

}
