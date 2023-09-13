package com.example.movies_app.data.movies.remote

import com.example.movies_app.data.core.remote.dto.PosterDto
import com.example.movies_app.data.core.remote.dto.RatingDto
import com.example.movies_app.data.core.remote.dto.VotesDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "poster") val posterDto: PosterDto,
    @Json(name = "rating") val ratingDto: RatingDto,
    @Json(name = "votes") val votesDto: VotesDto
)