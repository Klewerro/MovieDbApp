package com.klewerro.moviedbapp.core.data.remote

import com.klewerro.moviedbapp.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class TheMovieDbAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        val apiKey = BuildConfig.API_KEY
        val token = "Bearer $apiKey"
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", token)
            .build()

        return chain.proceed(newRequest)
    }
}
