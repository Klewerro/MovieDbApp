package com.klewerro.moviedbapp.core.domain

data class Movie(
    val adult: Boolean,
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
    val voteCount: Int
)
