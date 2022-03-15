package ru.elron.weather.ui.weather

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.libresources.LifecycleDialogFragment
import ru.elron.libresources.R
import ru.elron.weather.databinding.FragmentWeatherBinding
import ru.elron.weather.extensions.showToast

class WeatherFragment : BaseFragment<WeatherEntity, WeatherState, WeatherEvent>(),
    LifecycleDialogFragment.Builder {

    companion object {
        private const val DIALOG_ERROR_UNKNOWN = 100
        private const val DIALOG_ADD_FAVORITE_ERROR = 101
    }

    private val args: WeatherFragmentArgs by navArgs()

    private lateinit var binding: FragmentWeatherBinding
    val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(
            requireActivity().application,
            this,
            cityId = args.cityId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        binding.entity = viewModel.entity

        return binding.root
    }

    override fun onEvent(event: WeatherEvent) {
        when (event) {
            WeatherEvent.Back -> findNavController().navigateUp()
            WeatherEvent.ErrorUnknown -> {
                LifecycleDialogFragment()
                    .withId(DIALOG_ERROR_UNKNOWN)
                    .withChildFragmentId(
                        ru.elron.weather.R.id.nav_host_fragment_activity_main,
                        id
                    )
                    .show(requireActivity())
            }
            WeatherEvent.ShowDialogAddFavoriteError -> {
                LifecycleDialogFragment()
                    .withId(DIALOG_ADD_FAVORITE_ERROR)
                    .withChildFragmentId(
                        ru.elron.weather.R.id.nav_host_fragment_activity_main,
                        id
                    )
                    .show(requireActivity())
            }
            WeatherEvent.ShowDialogAddFavoriteSuccess -> {
                showToast(R.string.toast_success)
            }
            WeatherEvent.ShowDialogRemoveFavoriteSuccess -> {
                showToast(R.string.toast_success)
            }
            WeatherEvent.ShowScreenForecast -> {
                findNavController().navigate(
                    WeatherFragmentDirections.actionWeatherFragmentToForecastFragment(
                        args.cityId
                    )
                )
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
            DIALOG_ADD_FAVORITE_ERROR -> {
                builder.setTitle(R.string.dialog_add_favorite_error_title)
                builder.setMessage(R.string.dialog_add_favorite_error_message)
                builder.setPositiveButton(R.string.button_ok, null)
            }
        }

        dialog = builder.create()
        return dialog
    }

    override fun getBaseViewModel(): BaseViewModel<WeatherEntity, WeatherState, WeatherEvent> =
        viewModel
}
