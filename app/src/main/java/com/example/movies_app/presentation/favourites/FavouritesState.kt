package com.example.movies_app.presentation.favourites

import com.example.movies_app.domain.movies.Movie

data class FavouritesState(
    val movies: List<Movie> = emptyList(),
    val isNotFoundStubVisible: Boolean = false,
    val isFavouritesCleared: Boolean = false,
    val isClearQueryButtonVisible: Boolean = false,
)