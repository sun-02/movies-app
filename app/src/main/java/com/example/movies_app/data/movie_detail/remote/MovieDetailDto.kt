package com.example.movies_app.data.movie_detail.remote

import com.example.movies_app.data.core.remote.dto.CountryDto
import com.example.movies_app.data.core.remote.dto.GenreDto
import com.example.movies_app.data.core.remote.dto.PersonDto
import com.example.movies_app.data.core.remote.dto.PosterDto
import com.example.movies_app.data.core.remote.dto.PremiereDto
import com.example.movies_app.data.core.remote.dto.RatingDto
import com.example.movies_app.data.core.remote.dto.VideosDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieDetailDto(
    @Json(name = "description") val description: String,
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "persons") val personDtos: List<PersonDto>,
    @Json(name = "poster") val posterDto: PosterDto,
    @Json(name = "premiere") val premiereDto: PremiereDto,
    @Json(name = "rating") val ratingDto: RatingDto,
    @Json(name = "videos") val videosDto: VideosDto,
    @Json(name = "genres") val genreDtos: List<GenreDto>,
    @Json(name = "countries") val countryDtos: List<CountryDto>,
)