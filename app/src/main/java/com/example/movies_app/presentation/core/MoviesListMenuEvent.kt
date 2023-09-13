package com.example.movies_app.presentation.core

sealed interface MoviesListMenuEvent {
    data object SortByDefault : MoviesListMenuEvent
    data object SortByName : MoviesListMenuEvent
    data object SortByImbdRating : MoviesListMenuEvent
    data object SortByImbdVotes : MoviesListMenuEvent
}