package com.klewerro.moviedbapp.core.fake

import com.klewerro.moviedbapp.core.data.remote.MovieApi
import com.klewerro.moviedbapp.core.data.remote.dto.CurrentlyPlayingResponseDto
import com.klewerro.moviedbapp.core.data.remote.dto.DatesDto
import com.klewerro.moviedbapp.core.util.testData.MovieDtoTestData
import kotlinx.coroutines.delay

class FakeMovieApi : MovieApi {

    var exception: Exception? = null
    var response = CurrentlyPlayingResponseDto(
        DatesDto(
            "2024-09-25",
            "2024-08-14"
        ),
        page = 1,
        movieDtos = listOf(MovieDtoTestData.movie1, MovieDtoTestData.movie2),
        totalPages = 1,
        totalResults = 2
    )

    override suspend fun getCurrentlyPlaying(page: Int): CurrentlyPlayingResponseDto {
        delay(1_000)
        exception?.let {
            throw it
        }
        return response
    }

    fun setTotalPages(totalPages: Int) {
        response = response.copy(totalPages = totalPages)
    }
}
