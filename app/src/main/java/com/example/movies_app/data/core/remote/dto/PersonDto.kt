package com.example.movies_app.data.core.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonDto(
    @Json(name = "description") val description: String?,
    @Json(name = "enName") val enName: String?,
    @Json(name = "enProfession") val enProfession: String,
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "photo") val photo: String,
    @Json(name = "profession") val profession: String
)