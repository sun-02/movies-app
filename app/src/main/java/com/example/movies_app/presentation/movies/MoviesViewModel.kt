package com.example.movies_app.presentation.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.data.core.MoviesRepository
import com.example.movies_app.domain.movies.Movie
import com.example.movies_app.presentation.core.MoviesListMenuEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    val repository: MoviesRepository
) : ViewModel() {

    private val _state: MutableStateFlow<MoviesState> = MutableStateFlow(MoviesState())
    val state: StateFlow<MoviesState> = _state.asStateFlow()

    private var lastMenuEvent: MoviesListMenuEvent = MoviesListMenuEvent.SortByDefault

    @Volatile
    private var defaultSortedMovies: List<Movie> = emptyList()

    init {
        viewModelScope.launch {
            defaultSortedMovies = repository.getMovies()
            val hasFavourites = defaultSortedMovies.any { it.isFavourite }
            _state.update {
                it.copy(
                    movies = defaultSortedMovies,
                    isFavouritesButtonVisible = hasFavourites,
                    isEmptyListStubVisible = defaultSortedMovies.isEmpty()
                )
            }
        }
    }

    fun onMoviesListChanged() {
        viewModelScope.launch {
            val favourites = repository.getFavouriteMovies()
            _state.update {
                it.copy(
                    isFavouritesButtonVisible = favourites.isNotEmpty()
                )
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        viewModelScope.launch {
            val filteredList = repository.queryMovies(newQuery)
            defaultSortedMovies = filteredList
            val sortedList = if (lastMenuEvent == MoviesListMenuEvent.SortByDefault) {
                defaultSortedMovies
            } else {
                filteredList.sortBy(lastMenuEvent)
            }
            _state.update {
                it.copy(
                    movies = sortedList,
                    isNotFoundStubVisible = filteredList.isEmpty(),
                    isClearQueryButtonVisible = newQuery.isNotEmpty(),
                )
            }
        }
    }

    fun onItemLiked(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(isFavourite = !movie.isFavourite))
            val movie0 = repository.getMovieById(movie.id.value)
            val updatedList = state.value.movies.map { if (it.id == movie0.id) movie0 else it }
            val hasFavourites = repository.getMovies().any { it.isFavourite }
            _state.update { state ->
                state.copy(
                    movies = updatedList,
                    isNotFoundStubVisible = updatedList.isEmpty(),
                    isFavouritesButtonVisible = hasFavourites
                )
            }
        }
    }

    fun onMenuEvent(event: MoviesListMenuEvent) {
        if (event == lastMenuEvent) return
        _state.update { state ->
            state.copy(
                movies = state.movies.sortBy(event)
            )
        }

    }

    private fun List<Movie>.sortBy(event: MoviesListMenuEvent): List<Movie> {
        lastMenuEvent = event
        return when (event) {
            MoviesListMenuEvent.SortByDefault -> {
                // rank в ответе кинопоиска отсутствует,
                // поэтому вместо rank используется сортировка по-умолчанию
                defaultSortedMovies
            }

            MoviesListMenuEvent.SortByImbdRating -> {
                sortedBy { it.title }
            }

            MoviesListMenuEvent.SortByImbdVotes -> {
                sortedBy { it.imdbRating }
            }

            MoviesListMenuEvent.SortByName -> {
                sortedBy { it.imdbVotes }
            }
        }
    }
}