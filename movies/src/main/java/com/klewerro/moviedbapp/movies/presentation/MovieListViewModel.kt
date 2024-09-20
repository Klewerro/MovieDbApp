package com.klewerro.moviedbapp.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(movieRepository: MovieRepository) : ViewModel() {

    val currentlyPlayingMoviesPages = movieRepository
        .observeCurrentlyPlayingMoviesPages(viewModelScope)
}
