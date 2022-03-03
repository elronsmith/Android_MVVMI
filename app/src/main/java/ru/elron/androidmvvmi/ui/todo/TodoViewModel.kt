package ru.elron.androidmvvmi.ui.todo

import android.app.Application
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import ru.elron.androidmvvmi.R
import ru.elron.androidmvvmi.extensions.toTodoObservable
import ru.elron.androidmvvmi.observable.KeylayoutItemViewHolder
import ru.elron.androidmvvmi.observable.TodoObservable
import ru.elron.libdb.TodoManager
import ru.elron.libmvi.BaseViewModel
import ru.elron.libresources.AObservable
import ru.elron.libresources.OnItemClickViewHolderCallback
import ru.elron.libresources.RecyclerAdapter

class TodoViewModel(application: Application, stateHandle: SavedStateHandle) :
    BaseViewModel<TodoEntity, TodoState, TodoEvent>(
        application,
        stateHandle,
        "todo_entity",
        TodoState.Nothing
    ), OnItemClickViewHolderCallback {

    private val manager: TodoManager by KoinJavaComponent.inject(TodoManager::class.java)

    val adapter = RecyclerAdapter<TodoObservable>()

    init {
        KeylayoutItemViewHolder.addViewHolder(adapter.holderBuilderArray, this)
    }

    override fun getNewEntity(): TodoEntity = TodoEntity()

    fun updateEmptyVisibility() {
        entity.emptyVisible.set(adapter.observableList.isEmpty())
    }

    fun performGetList() {
        entity.progressVisible.set(true)
        viewModelScope.launch(Dispatchers.IO) {
            val list = manager.getList()
            adapter.observableList.clear()

            list.forEach {
                val o = it.toTodoObservable()
                adapter.observableList.add(o)
            }

            withContext(Dispatchers.Main) {
                entity.progressVisible.set(false)
                adapter.notifyDataSetChanged()
                updateEmptyVisibility()
            }
        }
    }

    override fun onItemClick(v: View?, observable: AObservable, position: Int) {
        when (v) {
            is CheckBox -> {
                val o = observable as TodoObservable
                o.isChecked = v.isChecked
            }
            else -> {
                when (v?.id) {
                    R.id.text -> {
                        val id = (observable as TodoObservable).id
                        eventLiveData.postValue(TodoEvent.ShowScreenEdit(id))
                    }
                    R.id.remove -> {
                        val o = observable as TodoObservable
                        val text = o.text.replace("\n", "").take(10)
                        eventLiveData.postValue(TodoEvent.Remove(position, text))
                    }
                }
            }
        }
    }

    fun performRemove(index: Int) {
        entity.progressVisible.set(true)
        viewModelScope.launch(Dispatchers.IO) {
            val o = adapter.observableList[index]
            manager.remove(o.id)
            adapter.observableList.removeAt(index)

            withContext(Dispatchers.Main) {
                entity.progressVisible.set(false)
                adapter.notifyItemRemoved(index)
                updateEmptyVisibility()
            }
        }
    }

    override fun getObservable(position: Int): AObservable = adapter.observableList[position]
}

class TodoViewModelFactory(
    private val application: Application,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle = Bundle()
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return TodoViewModel(
            application,
            handle
        ) as T
    }
}
