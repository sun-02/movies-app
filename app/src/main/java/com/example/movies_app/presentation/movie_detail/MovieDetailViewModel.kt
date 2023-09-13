package com.example.movies_app.presentation.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.data.core.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    val repository: MoviesRepository
) : ViewModel() {

    private val _state: MutableStateFlow<MovieDetailState> = MutableStateFlow(MovieDetailState())
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            val movieDetail = async { repository.getMovieDetailById(id) }
            val savedMovie = async { repository.getMovieById(id) }
            val movieDetailResult = movieDetail.await()
            _state.update {
                it.copy(
                    movieDetail = movieDetailResult.getOrNull(),
                    movieDetailError = movieDetailResult.exceptionOrNull()?.message,
                    isFavourite = savedMovie.await().isFavourite
                )
            }
            withContext(Dispatchers.Main) {
                Timber.d("D/getMovieDetail state: ${state.value}")
            }
        }
    }

    fun onItemLiked(id: Int) {
        viewModelScope.launch {
            val movie = repository.getMovieById(id)
            val movie0 = movie.copy(isFavourite = !movie.isFavourite)
            repository.updateMovie(movie0)
            val movie1 = repository.getMovieById(id)
            _state.update {
                it.copy(
                    isFavourite = movie1.isFavourite
                )
            }
            Timber.d("D/onItemUnliked state: ${state.value}")
        }
    }
}