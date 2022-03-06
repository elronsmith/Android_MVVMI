package ru.elron.weather.ui.search

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.libresources.LifecycleDialogFragment
import ru.elron.libresources.R
import ru.elron.weather.App
import ru.elron.weather.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<SearchEntity, SearchState, SearchEvent>(),
    LifecycleDialogFragment.Builder {

    companion object {
        private const val DIALOG_ERROR = 100
        private const val DIALOG_ERROR_UNKNOWN = 101
    }

    private lateinit var binding: FragmentSearchBinding
    val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            App.INSTANCE,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.entity = viewModel.entity

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = viewModel.adapter

        return binding.root
    }

    override fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.ShowDialogError -> {
                LifecycleDialogFragment()
                    .withId(DIALOG_ERROR)
                    .withChildFragmentId(
                        ru.elron.weather.R.id.nav_host_fragment_activity_main,
                        id
                    )
                    .withMessage(event.message)
                    .show(requireActivity())
            }
            SearchEvent.ShowDialogErrorUnknown -> {
                LifecycleDialogFragment()
                    .withId(DIALOG_ERROR_UNKNOWN)
                    .withChildFragmentId(
                        ru.elron.weather.R.id.nav_host_fragment_activity_main,
                        id
                    )
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
            DIALOG_ERROR -> {
                builder.setTitle(R.string.dialog_error_unknown_title)
                builder.setMessage(dialogFragment.getBundleMessage())
                builder.setPositiveButton(R.string.button_ok, null)
            }
            DIALOG_ERROR_UNKNOWN -> {
                builder.setTitle(R.string.dialog_error_unknown_title)
                builder.setMessage(R.string.dialog_error_unknown_message)
                builder.setPositiveButton(R.string.button_ok, null)
            }
        }

        dialog = builder.create()
        return dialog
    }

    override fun getBaseViewModel(): BaseViewModel<SearchEntity, SearchState, SearchEvent> =
        viewModel
}
