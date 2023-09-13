package com.example.movies_app.domain.movies

import com.example.movies_app.domain.core.KinopoiskId

data class Movie(
    val id: KinopoiskId,
    val title: String,
    val imdbRating: Double,
    val imdbVotes: Int,
    val previewUrl: String,
    val isFavourite: Boolean
)
