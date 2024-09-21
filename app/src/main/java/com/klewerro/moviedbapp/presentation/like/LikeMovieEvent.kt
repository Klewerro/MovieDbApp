package com.klewerro.moviedbapp.presentation.like

import com.klewerro.moviedbapp.core.domain.Movie

sealed class LikeMovieEvent {
    data class LikeMovie(val movie: Movie) : LikeMovieEvent()
    data class ObserveMovieLikeStatus(val movieId: Int) : LikeMovieEvent()
    data object StopObservingMovieLikeStatus : LikeMovieEvent()
    data object DismissLikeChanged : LikeMovieEvent()
}
