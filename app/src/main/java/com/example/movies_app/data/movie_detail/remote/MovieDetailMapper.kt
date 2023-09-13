package com.example.movies_app.data.movie_detail.remote

import com.example.movies_app.domain.movie_detail.MovieDetail
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun MovieDetailDto.toMovieDetail() =
    MovieDetail(
        id = this.id,
        title = this.name,
        description = this.description,
        posterUrl = this.posterDto.url,
        trailerUrl = this.videosDto.trailerDtos.let { if (it.isNotEmpty()) it[0].url else "" },
        genres = this.genreDtos.joinToString { it.name },
        releaseDate = LocalDate.parse(this.premiereDto.russia, DateTimeFormatter.ISO_DATE_TIME),
        imdbRating = this.ratingDto.imdb,
        countries = this.countryDtos.joinToString { it.name },
        actors = this.personDtos.filter { it.enProfession == "actor" }.map { it.toMovieActor() },
    )