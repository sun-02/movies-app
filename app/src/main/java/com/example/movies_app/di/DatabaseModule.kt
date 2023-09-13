package com.example.movies_app.di

import android.content.Context
import com.example.movies_app.data.core.MoviesRepository
import com.example.movies_app.data.core.MoviesRepositoryImpl
import com.example.movies_app.data.core.local.MoviesDao
import com.example.movies_app.data.core.local.MoviesDatabase
import com.example.movies_app.data.core.local.MoviesLocalSource
import com.example.movies_app.data.core.local.MoviesLocalSourceImpl
import com.example.movies_app.data.core.remote.MoviesRemoteSource
import com.example.movies_app.data.core.remote.MoviesRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule0 {

    @Binds
    fun bindMoviesLocalSourceImpl(
        moviesLocalSourceImpl: MoviesLocalSourceImpl
    ): MoviesLocalSource

    @Binds
    fun bindMoviesRemoteSourceImpl(
        moviesRemoteSourceImpl: MoviesRemoteSourceImpl
    ): MoviesRemoteSource

    @Binds
    fun bindMoviesRepositoryImpl(
        moviesRepositoryImpl: MoviesRepositoryImpl
    ): MoviesRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMoviesDb(
        @ApplicationContext appContext: Context
    ): MoviesDatabase = MoviesDatabase.create(appContext)


    @Provides
    fun provideMoviesDao(
        moviesDatabase: MoviesDatabase
    ): MoviesDao = moviesDatabase.moviesDao()
}