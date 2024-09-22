package com.klewerro.moviedbapp.core.presentation.like

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klewerro.moviedbapp.core.domain.LikeResult
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.core.domain.dispatcher.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseLikeMovieViewModel(
    private val movieRepository: MovieRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _likeStateChanged = MutableStateFlow<LikeChanged>(LikeChanged.Unchanged)
    val likeStateChanged = _likeStateChanged.asStateFlow()

    fun onLikeMovieEvent(event: LikeMovieEvent) {
        when (event) {
            is LikeMovieEvent.LikeMovie -> likeMovie(event.movie)
            LikeMovieEvent.DismissChangedLike -> dismissLikeChanged()
        }
    }

    private fun likeMovie(movie: Movie) {
        viewModelScope.launch(dispatchers.io) {
            val likeResult = movieRepository.likeMovie(movie)
            _likeStateChanged.update {
                when (likeResult) {
                    LikeResult.LIKED -> LikeChanged.Liked(movie.title)
                    LikeResult.LIKE_REMOVED -> LikeChanged.LikeRemoved(movie.title)
                    LikeResult.ERROR -> LikeChanged.Error(movie.title)
                }
            }
        }
    }

    private fun dismissLikeChanged() {
        _likeStateChanged.update {
            LikeChanged.Unchanged
        }
    }
}
