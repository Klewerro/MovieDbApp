package com.klewerro.moviedbapp.movies.presentation

import androidx.lifecycle.viewModelScope
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.core.domain.dispatcher.DispatcherProvider
import com.klewerro.moviedbapp.core.presentation.like.BaseLikeMovieViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    movieRepository: MovieRepository,
    dispatchers: DispatcherProvider
) : BaseLikeMovieViewModel(movieRepository, dispatchers) {

    private val _firstVisibleScrollIndex = MutableStateFlow(0)
    val firstVisibleScrollIndex = _firstVisibleScrollIndex.asStateFlow()

    val currentlyPlayingMoviesPages = movieRepository
        .observeCurrentlyPlayingMoviesPages(viewModelScope)

    fun updateFirstVisibleScrollIndex(index: Int) {
        _firstVisibleScrollIndex.update {
            index
        }
    }
}
