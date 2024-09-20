package com.klewerro.moviedbapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klewerro.moviedbapp.core.domain.LikeResult
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.presentation.like.LikeMovieEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeMovieViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private var observingJob: Job? = null
    private val isMovieLiked = MutableStateFlow<Boolean?>(null)
    private val likeChanged = MutableStateFlow<LikeChanged>(LikeChanged.Unchanged)

    val likeMovieState = combine(
        isMovieLiked,
        likeChanged
    ) { isMovieLiked, likeChangedStateValue ->
        LikeMovieState(
            isMovieLiked,
            likeChangedStateValue
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10_000L), LikeMovieState())

    fun onEvent(event: LikeMovieEvent) {
        when (event) {
            is LikeMovieEvent.LikeMovie -> likeMovie(event.movie)
            is LikeMovieEvent.ObserveMovieLikeStatus -> observeMovieLikeStatus(event.movieId)
            LikeMovieEvent.StopObservingMovieLikeStatus -> stopObservingMovieLikeStatus()
            LikeMovieEvent.DismissLikeChanged -> dismissLikeChanged()
        }
    }

    private fun likeMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
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

    private fun observeMovieLikeStatus(movieId: Int) {
        if (observingJob != null) {
            cancelObservingJob()
        }

        observingJob = viewModelScope.launch(Dispatchers.IO) {
            movieRepository.observeMovieLikedStatus(movieId)
                .onEach { isLiked ->
                    isMovieLiked.update {
                        isLiked
                    }
                }
                .launchIn(this)
        }
    }

    private fun stopObservingMovieLikeStatus() {
        cancelObservingJob()
        isMovieLiked.update { null }
    }

    private fun dismissLikeChanged() {
        likeChanged.update {
            LikeChanged.Unchanged
        }
    }

    private fun cancelObservingJob() {
        observingJob?.cancel()
        observingJob = null
    }
}
