package com.example.movies_app.presentation.movies

import com.example.movies_app.domain.movies.Movie

data class MoviesState(
    val movies: List<Movie> = emptyList(),
    val isEmptyListStubVisible: Boolean = true,
    val isFavouritesButtonVisible: Boolean = false,
    val isNotFoundStubVisible: Boolean = false,
    val isClearQueryButtonVisible: Boolean = false,
)