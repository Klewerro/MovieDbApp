package com.klewerro.moviedbapp.core.presentation.like

sealed class LikeChanged {
    data object Unchanged : LikeChanged()
    data class Liked(val movieTitle: String) : LikeChanged()
    data class LikeRemoved(val movieTitle: String) : LikeChanged()
    data class Error(val movieTitle: String) : LikeChanged()
}
