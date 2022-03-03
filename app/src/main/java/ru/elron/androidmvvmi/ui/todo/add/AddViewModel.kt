package ru.elron.androidmvvmi.ui.todo.add

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import ru.elron.libdb.TodoManager
import ru.elron.libmvi.BaseViewModel

class AddViewModel(application: Application, stateHandle: SavedStateHandle, val id: Long) :
    BaseViewModel<AddEntity, AddState, AddEvent>(
        application,
        stateHandle,
        "todo_add_entity",
        AddState.Nothing
    ) {

    private val manager: TodoManager by KoinJavaComponent.inject(TodoManager::class.java)

    private val isEditing: Boolean
        get() = id != 0L

    override fun getNewEntity(): AddEntity = AddEntity()

    override fun onCreateView() {
        if (isEditing)
            performGetTodo(id)
    }

    fun performGetTodo(id: Long) {
        entity.progressVisible.set(true)
        viewModelScope.launch(Dispatchers.IO) {
            manager.getByIdOrNull(id)?.let {
                entity.text.set(it.text)
            }

            withContext(Dispatchers.Main) {
                entity.progressVisible.set(false)
            }
        }
    }

    fun performSave() {
        if (entity.progressVisible.get())
            return

        entity.progressVisible.set(true)
        viewModelScope.launch(Dispatchers.IO) {
            val text = entity.text.get() ?: ""

            if (isEditing)
                manager.update(id, text)
            else
                manager.add(text)

            eventLiveData.postValue(AddEvent.Back)
        }
    }
}

class AddViewModelFactory(
    private val application: Application,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle = Bundle(),
    val id: Long
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return AddViewModel(
            application,
            handle,
            id
        ) as T
    }
}
