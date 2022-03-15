package ru.elron.androidmvvmi.ui.todo.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.elron.androidmvvmi.databinding.FragmentTodoAddBinding
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.libresources.R

class AddFragment : BaseFragment<AddEntity, AddState, AddEvent>() {

    private val args: AddFragmentArgs by navArgs()

    private lateinit var binding: FragmentTodoAddBinding
    val viewModel: AddViewModel by viewModels {
        AddViewModelFactory(
            requireActivity().application,
            this,
            id = args.id
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoAddBinding.inflate(inflater, container, false)
        binding.entity = viewModel.entity

        binding.toolbar.inflateMenu(R.menu.todo_add)
        binding.toolbar.setNavigationOnClickListener { back() }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_save -> {
                    viewModel.performSave()
                }
            }
            true
        }

        binding.editText.requestFocus()

        return binding.root
    }

    override fun onEvent(event: AddEvent) {
        when (event) {
            AddEvent.Back -> back()
        }

    }

    fun back() = findNavController().navigateUp()

    override fun getBaseViewModel(): BaseViewModel<AddEntity, AddState, AddEvent> =
        viewModel
}
