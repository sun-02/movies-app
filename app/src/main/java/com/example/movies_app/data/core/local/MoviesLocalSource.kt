package com.example.movies_app.data.core.local

import com.example.movies_app.data.movies.local.MovieEntity
import javax.inject.Inject


interface MoviesLocalSource {
    suspend fun getMovies(): List<MovieEntity>
    suspend fun getFavouriteMovies(): List<MovieEntity>
    suspend fun queryMovies(query: String): List<MovieEntity>
    suspend fun queryFavouriteMovies(query: String): List<MovieEntity>
    suspend fun insertMovies(movieEntities: List<MovieEntity>): List<Long>
    suspend fun updateMovie(movieEntity: MovieEntity)
    suspend fun updateMovies(movieEntities: List<MovieEntity>)
    suspend fun getMovieById(id: Int): MovieEntity
}

class MoviesLocalSourceImpl @Inject constructor(
    val moviesDao: MoviesDao
) : MoviesLocalSource {
    override suspend fun getMovies(): List<MovieEntity> =
        moviesDao.getMovies()

    override suspend fun getFavouriteMovies(): List<MovieEntity> =
        moviesDao.getFavouriteMovies()

    override suspend fun queryMovies(query: String): List<MovieEntity> =
        moviesDao.queryMovies(query)

    override suspend fun queryFavouriteMovies(query: String): List<MovieEntity> =
        moviesDao.queryFavouriteMovies(query)

    override suspend fun insertMovies(movieEntities: List<MovieEntity>): List<Long> =
        moviesDao.insertMovies(movieEntities)

    override suspend fun updateMovie(movieEntity: MovieEntity) {
        moviesDao.updateMovie(movieEntity)
    }

    override suspend fun updateMovies(movieEntities: List<MovieEntity>) {
        moviesDao.updateMovies(movieEntities)
    }

    override suspend fun getMovieById(id: Int): MovieEntity =
        moviesDao.getMovieById(id)
}