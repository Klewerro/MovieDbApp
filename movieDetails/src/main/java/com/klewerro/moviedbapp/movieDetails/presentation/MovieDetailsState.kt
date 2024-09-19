package com.klewerro.moviedbapp.movieDetails.presentation

import com.klewerro.moviedbapp.core.domain.Movie

data class MovieDetailsState(val movie: Movie, val popularityIndex: Int = 0)
