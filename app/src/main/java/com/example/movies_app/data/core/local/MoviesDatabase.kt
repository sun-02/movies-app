package com.example.movies_app.data.core.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movies_app.data.movies.local.MovieEntity
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = arrayOf(MovieEntity::class), version = 1)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {

        fun create(@ApplicationContext appContext: Context): MoviesDatabase =
            Room.databaseBuilder(
                appContext,
                MoviesDatabase::class.java,
                "movies_database"
            ).fallbackToDestructiveMigration()
                .build()
    }
}