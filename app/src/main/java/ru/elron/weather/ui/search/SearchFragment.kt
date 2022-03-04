package ru.elron.weather.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.elron.libmvi.BaseFragment
import ru.elron.libmvi.BaseViewModel
import ru.elron.weather.App
import ru.elron.weather.databinding.FragmentSearchBinding

class SearchFragment : BaseFragment<SearchEntity, SearchState, SearchEvent>() {

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

        return binding.root
    }

    override fun getBaseViewModel(): BaseViewModel<SearchEntity, SearchState, SearchEvent> =
        viewModel
}