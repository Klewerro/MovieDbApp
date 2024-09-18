package com.klewerro.moviedbapp.core.data.remote

import com.klewerro.moviedbapp.core.data.remote.dto.CurrentlyPlayingResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/now_playing")
    suspend fun getCurrentlyPlaying(@Query("page") page: Int): CurrentlyPlayingResponseDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}
