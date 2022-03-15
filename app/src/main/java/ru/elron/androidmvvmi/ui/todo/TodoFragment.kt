package ru.elron.androidmvvmi.ui.todo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elron.androidmvvmi.databinding.FragmentTodoBinding
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.libresources.LifecycleDialogFragment
import ru.elron.libresources.R

class TodoFragment : BaseFragment<TodoEntity, TodoState, TodoEvent>(),
    LifecycleDialogFragment.Builder {
    companion object {
        private const val DIALOG_DELETE = 100
    }

    private lateinit var binding: FragmentTodoBinding
    val viewModel: TodoViewModel by viewModels {
        TodoViewModelFactory(
            requireActivity().application,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        binding.entity = viewModel.entity

        binding.toolbar.inflateMenu(R.menu.todo)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_add -> {
                    findNavController().navigate(TodoFragmentDirections.actionTodoFragmentToAddFragment())
                }
            }
            true
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = viewModel.adapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.performGetList()
    }

    override fun onEvent(event: TodoEvent) {
        when (event) {
            is TodoEvent.ShowScreenEdit -> {
                findNavController().navigate(
                    TodoFragmentDirections.actionTodoFragmentToAddFragment(
                        event.id
                    )
                )
            }
            is TodoEvent.Remove -> {
                LifecycleDialogFragment()
                    .withId(DIALOG_DELETE)
                    .withChildFragmentId(
                        ru.elron.androidmvvmi.R.id.nav_host_fragment_content_main,
                        id
                    )
                    .withIndex(event.index)
                    .withMessage(event.text)
                    .show(requireActivity())
            }
        }
    }

    override fun getLifecycleDialogInstance(
        id: Int,
        dialogFragment: LifecycleDialogFragment
    ): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val dialog: Dialog

        when (id) {
            DIALOG_DELETE -> {
                builder.setTitle(getString(R.string.dialog_todo_remove_title))
                builder.setMessage(
                    getString(
                        R.string.dialog_todo_remove_message,
                        dialogFragment.getBundleMessage()
                    )
                )
                builder.setPositiveButton(R.string.button_remove) { _, _ ->
                    val index = dialogFragment.getBundleIndex()
                    viewModel.performRemove(index)
                }
            }
        }

        dialog = builder.create()
        return dialog
    }

    override fun getBaseViewModel(): BaseViewModel<TodoEntity, TodoState, TodoEvent> =
        viewModel
}
