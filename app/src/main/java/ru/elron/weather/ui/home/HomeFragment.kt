package ru.elron.weather.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.weather.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeEntity, HomeState, HomeEvent>() {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            requireActivity().application,
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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = viewModel.adapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.performGetFavoriteList()
    }

    override fun onEvent(event: HomeEvent) {
        when(event) {
            is HomeEvent.ShowScreenWeather -> {
                findNavController().navigate(
                    HomeFragmentDirections.actionNavigationHomeToWeatherFragment(
                        event.cityId
                    )
                )
            }
        }
    }

    override fun getBaseViewModel(): BaseViewModel<HomeEntity, HomeState, HomeEvent> =
        viewModel
}
