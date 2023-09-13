package com.example.movies_app.data.core.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.movies_app.data.movies.local.MovieEntity

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movieEntities: List<MovieEntity>): List<Long>

    @Update
    suspend fun updateMovie(movieEntity: MovieEntity)

    @Update
    suspend fun updateMovies(movieEntities: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity")
    suspend fun getMovies(): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE is_favourite = 1")
    suspend fun getFavouriteMovies(): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE title LIKE '%' || :query || '%'")
    suspend fun queryMovies(query: String): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE title LIKE '%' || :query || '%' AND is_favourite = 1")
    suspend fun queryFavouriteMovies(query: String): List<MovieEntity>

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity
}