package com.klewerro.moviedbapp.movieDetails.presentation

sealed class MovieDetailsEvent {
    data object MovieDetails : MovieDetailsEvent()
    data object DismissChangedDetails : MovieDetailsEvent()
}
