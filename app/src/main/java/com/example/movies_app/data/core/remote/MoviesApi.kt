package com.example.movies_app.data.core.remote

import androidx.room.Dao
import com.example.movies_app.data.movie_detail.remote.KinopoiskMovieDetailsDto
import com.example.movies_app.data.movies.remote.KinopoiskMoviesDto
import retrofit2.http.GET
import retrofit2.http.Query

@Dao
interface MoviesApi {

    @GET("/v1.3/movie?selectFields=id%20name%20rating%20votes%20poster&page=1&limit=50&isSeries=false")
    suspend fun fetchMovies(): KinopoiskMoviesDto

    @GET("/v1.3/movie?selectFields=id%20name%20rating%20poster%20description%20premiere%20persons%20videos"+
            "%20genres%20countries&page=1&limit=1&isSeries=false")
    suspend fun fetchMovieDetailById(
        @Query("id") id: Int,
    ): KinopoiskMovieDetailsDto
}