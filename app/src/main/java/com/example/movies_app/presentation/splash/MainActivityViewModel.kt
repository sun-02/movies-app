package com.example.movies_app.presentation.splash

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.data.core.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _state: MutableStateFlow<MainActivityState> = MutableStateFlow(MainActivityState.Fetching)
    val state: StateFlow<MainActivityState> = _state.asStateFlow()

    private var timer: CountDownTimer? = null

    var moviesPersisted: Boolean = false
        private set

    fun persistMovies() {
        moviesPersisted = true
        timer?.cancel()
        Timber.d("D/Starting to fetch movies")
        viewModelScope.launch {
            moviesRepository.fetchMoviesToLocalSource()
                .onSuccess {
                    Timber.d("D/Successfully fetched movies")
                    _state.emit(MainActivityState.SuccessfullyFetchedToDatabase)
                }.onFailure {
                    Timber.d("D/Failed to fetch movies")
                    _state.emit(MainActivityState.ErrorFetchingToDatabase(it.message!!))
                    startTimer()
                }
        }
    }

    private fun startTimer() {
        timer?.cancel()
        Timber.d("D/Started countdown to retry fetch")
        timer = object : CountDownTimer(TIMER_COUNTDOWN, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000L).toInt() + 1
                Timber.d("D/$seconds to retry")
                _state.update {
                    MainActivityState.RepeatAfter(seconds)
                }
            }

            override fun onFinish() {
                _state.update {
                    MainActivityState.Fetching
                }
                Timber.d("D/Countdown ended. Retrying fetch")
                persistMovies()
            }
        }.start()
    }

    companion object {
        private const val TIMER_COUNTDOWN = 5000L
        private const val COUNTDOWN_INTERVAL = 1000L
    }
}