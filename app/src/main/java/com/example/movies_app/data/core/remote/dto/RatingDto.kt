package com.example.movies_app.data.core.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingDto(
    @Json(name = "await") val await: Any?,
    @Json(name = "filmCritics") val filmCritics: Double,
    @Json(name = "imdb") val imdb: Double,
    @Json(name = "kp") val kp: Double,
    @Json(name = "russianFilmCritics") val russianFilmCritics: Double
)