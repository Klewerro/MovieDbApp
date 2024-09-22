package com.klewerro.moviedbapp.movieDetails.presentation

import com.klewerro.moviedbapp.core.domain.Movie

data class MovieDetailsState(
    val movie: Movie,
    val popularityIndex: Int = 0,
    val likeChanged: LikeChanged = LikeChanged.Unchanged
)

sealed class LikeChanged {
    data object Unchanged : LikeChanged()
    data class Liked(val movieTitle: String) : LikeChanged()
    data class LikeRemoved(val movieTitle: String) : LikeChanged()
    data class Error(val movieTitle: String) : LikeChanged()
}
