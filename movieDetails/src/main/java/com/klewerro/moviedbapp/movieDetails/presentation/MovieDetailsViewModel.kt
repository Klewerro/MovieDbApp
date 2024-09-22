package com.klewerro.moviedbapp.movieDetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.core.domain.dispatcher.DispatcherProvider
import com.klewerro.moviedbapp.core.presentation.like.BaseLikeMovieViewModel
import com.klewerro.moviedbapp.core.presentation.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedState: SavedStateHandle,
    movieRepository: MovieRepository,
    dispatchers: DispatcherProvider
) : BaseLikeMovieViewModel(movieRepository, dispatchers) {

    private val movieDetailsScreenParams = savedState.toRoute<NavRoute.MovieDetailsScreen>(
        typeMap = NavRoute.movieDetailsScreenTypeMap
    )

    val state = combine(
        movieRepository.observeMovieLikedStatus(movieDetailsScreenParams.movie.id),
        likeStateChanged
    ) { isMovieLikedValue, likeChangedStateValue ->
        MovieDetailsState(
            movie = movieDetailsScreenParams.movie.copy(isLiked = isMovieLikedValue),
            popularityIndex = movieDetailsScreenParams.index + 1,
            likeChanged = likeChangedStateValue
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(10_000),
        MovieDetailsState(movieDetailsScreenParams.movie)
    )
}
