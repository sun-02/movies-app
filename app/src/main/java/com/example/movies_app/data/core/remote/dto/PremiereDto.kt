package com.example.movies_app.data.core.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PremiereDto(
    @Json(name = "russia") val russia: String,
    @Json(name = "world") val world: String
)