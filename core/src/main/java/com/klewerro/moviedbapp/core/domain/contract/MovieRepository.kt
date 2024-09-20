package com.klewerro.moviedbapp.core.domain.contract

import androidx.paging.PagingData
import com.klewerro.moviedbapp.core.domain.LikeResult
import com.klewerro.moviedbapp.core.domain.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun observeCurrentlyPlayingMoviesPages(scope: CoroutineScope): Flow<PagingData<Movie>>
    fun observeMovieLikedStatus(movieId: Int): Flow<Boolean>
    suspend fun likeMovie(movie: Movie): LikeResult
}
