package ru.elron.sensors.ui.sensor.orientation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.sensors.App
import ru.elron.sensors.databinding.FragmentOrientationBinding
import ru.elron.sensors.extensions.postPeriodically

class OrientationFragment : BaseFragment<OrientationEntity, OrientationState, OrientationEvent>() {

    private lateinit var binding: FragmentOrientationBinding
    val viewModel: OrientationViewModel by viewModels {
        OrientationViewModelFactory(
            App.INSTANCE,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrientationBinding.inflate(inflater, container, false)
        binding.entity = viewModel.entity

        binding.root.postPeriodically(500) {
            viewModel.update()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribe()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unsubscribe()
    }

    override fun getBaseViewModel(): BaseViewModel<OrientationEntity, OrientationState, OrientationEvent> =
        viewModel
}
