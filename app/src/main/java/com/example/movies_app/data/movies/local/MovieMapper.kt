package com.example.movies_app.data.movies.local

import com.example.movies_app.data.movies.remote.MovieDto
import com.example.movies_app.domain.core.KinopoiskId
import com.example.movies_app.domain.movies.Movie

fun MovieDto.toMovieEntity() =
    MovieEntity(
        id = this.id,
        title = this.name,
        imdbRating = this.ratingDto.imdb,
        imdbVotes = this.votesDto.imdb,
        previewUrl = this.posterDto.previewUrl,
        isFavourite = false,
    )

fun MovieEntity.toMovie() =
    Movie(KinopoiskId(id), title, imdbRating, imdbVotes, previewUrl, isFavourite)

fun Movie.toMovieEntity() =
    MovieEntity(id.value, title, imdbRating, imdbVotes, previewUrl, isFavourite)