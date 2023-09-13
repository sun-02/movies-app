package com.example.movies_app.data.core.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PosterDto(
    @Json(name = "previewUrl") val previewUrl: String,
    @Json(name = "url") val url: String
)