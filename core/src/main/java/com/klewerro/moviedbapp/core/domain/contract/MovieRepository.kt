package com.klewerro.moviedbapp.core.domain.contract

import androidx.paging.PagingData
import com.klewerro.moviedbapp.core.domain.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getCurrentlyPlayingMovies(): Flow<PagingData<Movie>>
}
