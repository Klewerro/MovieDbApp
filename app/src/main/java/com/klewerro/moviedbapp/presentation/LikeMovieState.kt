package com.klewerro.moviedbapp.presentation

data class LikeMovieState(
    val isMovieLiked: Boolean? = null,
    val likeChanged: LikeChanged = LikeChanged.Unchanged
)

sealed class LikeChanged {
    data object Unchanged : LikeChanged()
    data class Liked(val movieTitle: String) : LikeChanged()
    data class LikeRemoved(val movieTitle: String) : LikeChanged()
    data class Error(val movieTitle: String) : LikeChanged()
}
