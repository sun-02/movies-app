package com.example.movies_app.data.core.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideosDto(
    @Json(name = "teasers:") val teasers: List<Any>?,
    @Json(name = "trailers") val trailerDtos: List<TrailerDto>
)