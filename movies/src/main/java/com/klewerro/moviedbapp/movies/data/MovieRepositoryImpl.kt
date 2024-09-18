package com.klewerro.moviedbapp.movies.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.klewerro.moviedbapp.core.data.remote.MovieApi
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.core.util.ConfigConstants

class MovieRepositoryImpl(private val movieApi: MovieApi) : MovieRepository {

    override fun getCurrentlyPlayingMovies() = Pager(
        config = PagingConfig(
            pageSize = ConfigConstants.PAGING_CONFIG_PAGE_SIZE,
            prefetchDistance = ConfigConstants.PAGING_CONFIG_PREFETCH_DISTANCE
        ),
        pagingSourceFactory = { MoviesPagingSource(movieApi) }
    ).flow
}
