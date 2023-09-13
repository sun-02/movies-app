package com.example.movies_app.presentation.core

import com.example.movies_app.domain.core.KinopoiskId
import com.example.movies_app.domain.movies.Movie

interface MoviesListItemEvent {
    data class OnItemClick(val id: KinopoiskId) : MoviesListItemEvent
    data class OnLikeClick(val movie: Movie) : MoviesListItemEvent
}