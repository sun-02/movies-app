package com.example.movies_app.data.movie_detail.remote

import com.example.movies_app.data.core.remote.dto.PersonDto
import com.example.movies_app.domain.movie_detail.MovieActor

fun PersonDto.toMovieActor() =
    MovieActor(
        actorName = this.name,
        characterName = this.description,
        imageUrl = this.photo,
    )