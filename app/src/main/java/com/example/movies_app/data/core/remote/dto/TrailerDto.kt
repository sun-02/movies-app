package com.example.movies_app.data.core.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrailerDto(
    @Json(name = "name") val name: String,
    @Json(name = "site") val site: String,
    @Json(name = "type") val type: String,
    @Json(name = "url") val url: String
)