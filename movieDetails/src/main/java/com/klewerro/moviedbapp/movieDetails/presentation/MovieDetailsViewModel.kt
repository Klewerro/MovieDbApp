package com.klewerro.moviedbapp.movieDetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.klewerro.moviedbapp.core.domain.LikeResult
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.core.domain.dispatcher.DispatcherProvider
import com.klewerro.moviedbapp.core.presentation.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val likeChanged = MutableStateFlow<LikeChanged>(LikeChanged.Unchanged)

    private val movieDetailsScreenParams = savedState.toRoute<NavRoute.MovieDetailsScreen>(
        typeMap = NavRoute.movieDetailsScreenTypeMap
    )

    val state = combine(
        movieRepository.observeMovieLikedStatus(movieDetailsScreenParams.movie.id),
        likeChanged
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

    fun onEvent(event: MovieDetailsEvent) {
        when (event) {
            is MovieDetailsEvent.MovieDetails -> likeMovie(movieDetailsScreenParams.movie)
            MovieDetailsEvent.DismissChangedDetails -> dismissLikeChanged()
        }
    }

    private fun likeMovie(movie: Movie) {
        viewModelScope.launch(dispatchers.io) {
            val likeResult = movieRepository.likeMovie(movie)
            likeChanged.update {
                when (likeResult) {
                    LikeResult.LIKED -> LikeChanged.Liked(movie.title)
                    LikeResult.LIKE_REMOVED -> LikeChanged.LikeRemoved(movie.title)
                    LikeResult.ERROR -> LikeChanged.Error(movie.title)
                }
            }
        }
    }

    private fun dismissLikeChanged() {
        likeChanged.update {
            LikeChanged.Unchanged
        }
    }
}
