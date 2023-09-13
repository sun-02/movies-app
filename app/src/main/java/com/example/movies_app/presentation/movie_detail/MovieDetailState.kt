package com.example.movies_app.presentation.movie_detail

import com.example.movies_app.domain.movie_detail.MovieDetail

data class MovieDetailState(
    val movieDetail: MovieDetail? = null,
    val movieDetailError: String? = null,
    val isFavourite: Boolean = false
)
