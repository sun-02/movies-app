package com.example.movies_app.data.movies.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KinopoiskMoviesDto(
    @Json(name = "docs") val movieDtos: List<MovieDto>,
    @Json(name = "limit") val limit: Int,
    @Json(name = "page") val page: Int,
    @Json(name = "pages") val pages: Int,
    @Json(name = "total") val total: Int
)