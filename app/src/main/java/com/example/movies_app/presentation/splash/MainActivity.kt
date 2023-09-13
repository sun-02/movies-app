package com.example.movies_app.presentation.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.movies_app.R
import com.example.movies_app.databinding.ActivityMainBinding
import com.example.movies_app.presentation.core.extensions.addFragment
import com.example.movies_app.presentation.movies.MoviesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is MainActivityState.ErrorFetchingToDatabase -> {
                        binding.errorMessage.let { tv ->
                            tv.isVisible = true
                            tv.text = state.message
                        }
                    }

                    MainActivityState.Fetching -> {
                        binding.errorMessage.isVisible = false
                        binding.repeatAfter.isVisible = false
                    }

                    MainActivityState.SuccessfullyFetchedToDatabase -> {
                        runBlocking {
                            delay(800L)
                        }
                        addFragment<MoviesFragment>(R.id.container)
                    }

                    is MainActivityState.RepeatAfter -> {
                        binding.repeatAfter.let { tv ->
                            tv.isVisible = true
                            tv.text = getString(R.string.repeat_after, state.seconds)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        with (viewModel) {
            if (!moviesPersisted) persistMovies()
        }
    }
}