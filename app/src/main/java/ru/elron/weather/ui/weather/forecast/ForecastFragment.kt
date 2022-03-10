package ru.elron.weather.ui.weather.forecast

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.libresources.LifecycleDialogFragment
import ru.elron.libresources.R
import ru.elron.weather.App
import ru.elron.weather.databinding.FragmentForecastBinding

class ForecastFragment : BaseFragment<ForecastEntity, ForecastState, ForecastEvent>(),
    LifecycleDialogFragment.Builder {
    companion object {
        private const val DIALOG_ERROR_UNKNOWN = 100
    }

    private val args: ForecastFragmentArgs by navArgs()

    private lateinit var binding: FragmentForecastBinding
    val viewModel: ForecastViewModel by viewModels {
        ForecastViewModelFactory(
            App.INSTANCE,
            this,
            cityId = args.cityId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForecastBinding.inflate(inflater, container, false)
        binding.entity = viewModel.entity

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = viewModel.adapter

        return binding.root
    }

    override fun onEvent(event: ForecastEvent) {
        when (event) {
            ForecastEvent.Back -> findNavController().navigateUp()
            ForecastEvent.ErrorUnknown -> {
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
            DIALOG_ERROR_UNKNOWN -> {
                builder.setTitle(R.string.dialog_error_unknown_title)
                builder.setMessage(R.string.dialog_error_unknown_message)
                builder.setPositiveButton(R.string.button_ok, null)
            }
        }

        dialog = builder.create()
        return dialog
    }

    override fun getBaseViewModel(): BaseViewModel<ForecastEntity, ForecastState, ForecastEvent> =
        viewModel
}
