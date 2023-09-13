package com.example.movies_app.data.core.remote

import com.example.movies_app.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyAdder : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return chain.proceed(
            request.newBuilder()
                .header(API_TOKEN_KEY, BuildConfig.API_TOKEN_VALUE)
                .build()
        )
    }
    companion object {
        private const val API_TOKEN_KEY = "X-API-KEY"
    }
}