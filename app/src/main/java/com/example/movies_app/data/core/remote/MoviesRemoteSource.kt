package com.example.movies_app.data.core.remote

import com.example.movies_app.data.movie_detail.remote.MovieDetailDto
import com.example.movies_app.data.movies.remote.MovieDto
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

interface MoviesRemoteSource {
    suspend fun fetchMovies(): Result<List<MovieDto>>
    suspend fun fetchMovieDetailById(id: Int): Result<MovieDetailDto>
}

class MoviesRemoteSourceImpl @Inject constructor(
    val moviesApi: MoviesApi
) : MoviesRemoteSource {
    override suspend fun fetchMovies(): Result<List<MovieDto>> {
        return kotlin.runCatching {
            moviesApi.fetchMovies()
        }.map {
            Timber.d("D/Got movies from remote source: $it")
            it.movieDtos
        }.onFailure {
            Timber.e(it.message)
            if (it !is IOException) throw it
        }
    }

    override suspend fun fetchMovieDetailById(id: Int): Result<MovieDetailDto> {
        return kotlin.runCatching {
            moviesApi.fetchMovieDetailById(id)
        }.map {
            Timber.d("D/Got movie detail from remote source: $it")
            it.movieDetailDtos[0]
        }.onFailure {
            Timber.e(it.message)
            if (it !is IOException) throw it
        }
    }
}