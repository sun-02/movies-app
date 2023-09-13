package com.example.movies_app.data.movie_detail.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KinopoiskMovieDetailsDto(
    @Json(name = "docs") val movieDetailDtos: List<MovieDetailDto>,
    @Json(name = "limit") val limit: Int,
    @Json(name = "page") val page: Int,
    @Json(name = "pages") val pages: Int,
    @Json(name = "total") val total: Int
)