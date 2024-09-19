package com.klewerro.moviedbapp.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CurrentlyPlayingResponseDto(
    @SerializedName("dates")
    val dates: DatesDto,
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movieDtos: List<MovieDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
