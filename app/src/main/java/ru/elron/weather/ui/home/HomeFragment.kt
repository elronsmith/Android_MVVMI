package ru.elron.weather.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.weather.App
import ru.elron.weather.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeEntity, HomeState, HomeEvent>() {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            App.INSTANCE,
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.entity = viewModel.entity

        return binding.root
    }

    override fun getBaseViewModel(): BaseViewModel<HomeEntity, HomeState, HomeEvent> =
        viewModel
}
