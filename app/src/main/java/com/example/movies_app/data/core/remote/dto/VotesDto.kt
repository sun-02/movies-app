package com.example.movies_app.data.core.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VotesDto(
    @Json(name = "await") val await: Int,
    @Json(name = "filmCritics") val filmCritics: Int,
    @Json(name = "imdb") val imdb: Int,
    @Json(name = "kp") val kp: Int,
    @Json(name = "russianFilmCritics") val russianFilmCritics: Int
)