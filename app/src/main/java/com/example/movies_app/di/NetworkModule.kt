package com.example.movies_app.di

import com.example.movies_app.BuildConfig
import com.example.movies_app.data.core.remote.ApiKeyAdder
import com.example.movies_app.data.core.remote.MoviesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @AuthInterceptor
    fun provideAuthInterceptor(): Interceptor = ApiKeyAdder()

    @Provides
    @Singleton
    @LoggingInterceptor
    fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @AuthInterceptor apiKeyAdder: Interceptor,
        @LoggingInterceptor loggingInterceptor: Interceptor
    ): OkHttpClient = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .addInterceptor(apiKeyAdder)
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient.Builder()
        .addInterceptor(apiKeyAdder)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi = retrofit.create(MoviesApi::class.java)
}