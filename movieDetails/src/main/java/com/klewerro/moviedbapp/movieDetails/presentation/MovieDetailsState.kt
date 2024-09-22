package com.klewerro.moviedbapp.movieDetails.presentation

import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.presentation.like.LikeChanged

data class MovieDetailsState(
    val movie: Movie,
    val popularityIndex: Int = 0,
    val likeChanged: LikeChanged = LikeChanged.Unchanged
)
