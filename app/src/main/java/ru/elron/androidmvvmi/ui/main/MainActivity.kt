package ru.elron.androidmvvmi.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import ru.elron.androidmvvmi.App
import ru.elron.androidmvvmi.databinding.ActivityMainBinding
import ru.elron.libmvi.BaseActivity
import ru.elron.libmvi.BaseViewModel

class MainActivity : BaseActivity<MainEntity, MainState, MainEvent>() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            App.INSTANCE,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun getBaseViewModel(): BaseViewModel<MainEntity, MainState, MainEvent> = viewModel
}
