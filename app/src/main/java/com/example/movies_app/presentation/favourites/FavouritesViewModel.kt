package com.example.movies_app.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.data.core.MoviesRepository
import com.example.movies_app.domain.movies.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    val repository: MoviesRepository
) : ViewModel() {

    private val _state: MutableStateFlow<FavouritesState> = MutableStateFlow(FavouritesState())
    val state: StateFlow<FavouritesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val favourites = repository.getFavouriteMovies()
            _state.update {
                it.copy(
                    movies = favourites,
                    isFavouritesCleared = favourites.isEmpty(),
                )
            }
            Timber.d("Initial state: $state")
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        viewModelScope.launch {
            val filteredFavourites = repository.queryFavouriteMovies(newQuery)
            _state.update {
                it.copy(
                    movies = filteredFavourites,
                    isNotFoundStubVisible = filteredFavourites.isEmpty(),
                    isClearQueryButtonVisible = newQuery.isNotEmpty()
                )
            }
            Timber.d("onSearchQueryChanged state: $state")
        }
    }

    fun onItemUnliked(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(isFavourite = !movie.isFavourite))
            val favourites = repository.getFavouriteMovies()
            val updatedMovie = repository.getMovieById(movie.id.value)
            val updatedList = state.value.movies.map { m -> if (m.id == updatedMovie.id) updatedMovie else m }
            val filteredList = updatedList.filter { it.isFavourite }
            _state.update {
                it.copy(
                    movies = filteredList,
                    isNotFoundStubVisible = filteredList.isEmpty() && favourites.isNotEmpty(),
                    isFavouritesCleared = favourites.isEmpty(),
                )
            }
            Timber.d("onItemUnliked state: $state")
        }
    }

    fun onClearFavourites() {
        viewModelScope.launch {
            val favourites = repository.getFavouriteMovies()
            val nonFavoriteMovies = favourites.map { it.copy(isFavourite = false) }
            repository.updateMovies(nonFavoriteMovies)
            val emptyFavourites = repository.getFavouriteMovies()
            _state.update {
                it.copy(
                    movies = emptyFavourites,
                    isNotFoundStubVisible = false,
                    isFavouritesCleared = emptyFavourites.isEmpty(),
                )
            }
            Timber.d("onClearFavourites state: $state")
        }
    }
}