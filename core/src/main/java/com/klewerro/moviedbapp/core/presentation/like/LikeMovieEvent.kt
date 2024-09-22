package com.klewerro.moviedbapp.core.presentation.like

import com.klewerro.moviedbapp.core.domain.Movie

sealed class LikeMovieEvent {
    data class LikeMovie(val movie: Movie) : LikeMovieEvent()
    data object DismissChangedLike : LikeMovieEvent()
}
