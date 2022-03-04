package ru.elron.weather.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.elron.libmvi.BaseActivity
import ru.elron.libmvi.BaseViewModel
import ru.elron.weather.App
import ru.elron.weather.R
import ru.elron.weather.databinding.ActivityMainBinding

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

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_search
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun getBaseViewModel(): BaseViewModel<MainEntity, MainState, MainEvent> = viewModel
}
