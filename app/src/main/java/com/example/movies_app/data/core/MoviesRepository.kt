package com.example.movies_app.data.core

import com.example.movies_app.data.core.local.MoviesLocalSource
import com.example.movies_app.data.core.remote.MoviesRemoteSource
import com.example.movies_app.data.movie_detail.remote.toMovieDetail
import com.example.movies_app.data.movies.local.toMovie
import com.example.movies_app.data.movies.local.toMovieEntity
import com.example.movies_app.domain.movie_detail.MovieDetail
import com.example.movies_app.domain.movies.Movie
import timber.log.Timber
import java.lang.IllegalStateException
import javax.inject.Inject

interface MoviesRepository {
    suspend fun fetchMoviesToLocalSource(): Result<Unit>
    suspend fun getMovies(): List<Movie>
    suspend fun getFavouriteMovies(): List<Movie>
    suspend fun queryMovies(query: String): List<Movie>
    suspend fun queryFavouriteMovies(query: String): List<Movie>
    suspend fun updateMovie(movie: Movie)
    suspend fun updateMovies(movies: List<Movie>)
    suspend fun getMovieDetailById(id: Int): Result<MovieDetail>
    suspend fun getMovieById(id: Int): Movie
}

class MoviesRepositoryImpl @Inject constructor(
    val localSource: MoviesLocalSource,
    val remoteSource: MoviesRemoteSource
) : MoviesRepository {
    override suspend fun fetchMoviesToLocalSource(): Result<Unit> {
        val movieEntitiesResult = remoteSource.fetchMovies()
            .map {
                Timber.d("D/MovieDtos mapped to MovieEntities")
                it.map { movieDto -> movieDto.toMovieEntity() }
            }

        if (movieEntitiesResult.isFailure) {
            val e = movieEntitiesResult.exceptionOrNull()!!
            Timber.e("Movie entities returned exception ${e.message}")
            return Result.failure(e)
        }

        val movieEntities = movieEntitiesResult.getOrNull()!!

        val inserted = localSource.insertMovies(movieEntities)
        Timber.d("D/Inserted ${inserted.size} rows into database")
        return if (inserted.size == movieEntities.size) Result.success(Unit)
            else Result.failure(IllegalStateException("Fetched movies was not inserted"))
    }

    override suspend fun getMovies(): List<Movie> =
        localSource.getMovies().map { it.toMovie() }

    override suspend fun getFavouriteMovies(): List<Movie> =
        localSource.getFavouriteMovies().map { it.toMovie() }

    override suspend fun queryMovies(query: String): List<Movie> =
        localSource.queryMovies(query).map { it.toMovie() }

    override suspend fun queryFavouriteMovies(query: String): List<Movie> =
        localSource.queryFavouriteMovies(query).map { it.toMovie() }

    override suspend fun updateMovie(movie: Movie) {
        localSource.updateMovie(movie.toMovieEntity())
    }

    override suspend fun updateMovies(movies: List<Movie>) {
        localSource.updateMovies(movies.map { it.toMovieEntity() })
    }

    override suspend fun getMovieDetailById(id: Int): Result<MovieDetail> =
        remoteSource.fetchMovieDetailById(id).map { it.toMovieDetail() }

    override suspend fun getMovieById(id: Int): Movie =
        localSource.getMovieById(id).toMovie()
}