package com.example.movies_app.domain.movie_detail

import java.time.LocalDate

data class MovieDetail(
    val id: Int,
    val title: String,
    val description: String,
    val posterUrl: String,
    val trailerUrl: String,
    val genres: String,
    val releaseDate: LocalDate,
    val imdbRating: Double,
    val countries: String,
    val actors: List<MovieActor>
)
