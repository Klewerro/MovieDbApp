package com.klewerro.moviedbapp.core.domain

import com.klewerro.moviedbapp.core.data.remote.MovieApi
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val popularity: Double,
    val backdropPath: String?,
    val posterPath: String?,
    val genreIds: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val releaseDate: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val adult: Boolean,
    val isLiked: Boolean
) {
    val releaseYear = releaseDate.substringBefore('-')
    val backdrop500Url = MovieApi.IMAGE_500 + backdropPath
    val backdropOriginalUrl = MovieApi.IMAGE_ORIGINAL + backdropPath
    val poster500Url = MovieApi.IMAGE_500 + posterPath
    val posterOriginalUrl = MovieApi.IMAGE_ORIGINAL + posterPath
}
