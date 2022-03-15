package ru.elron.weather.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.elron.libmvi.BaseActivity
import ru.elron.libmvi.BaseViewModel
import ru.elron.weather.R
import ru.elron.weather.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainEntity, MainState, MainEvent>() {

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            application,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener {
            return@setOnItemSelectedListener when(it.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home, null, navOptions {
                        launchSingleTop = true
                        popUpTo(R.id.navigation_home)
                    })
                    true
                }
                R.id.navigation_search -> {
                    navController.navigate(R.id.navigation_search, null, navOptions {
                        launchSingleTop = true
                        popUpTo(R.id.navigation_home)
                    })
                    true
                }
                else -> true
            }
        }
    }

    override fun getBaseViewModel(): BaseViewModel<MainEntity, MainState, MainEvent> = viewModel
}
