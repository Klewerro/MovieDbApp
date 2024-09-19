package com.klewerro.moviedbapp.movies.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.klewerro.moviedbapp.core.data.remote.MovieApi
import com.klewerro.moviedbapp.core.data.remote.mapper.toMovie
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.util.ConfigConstants
import kotlinx.coroutines.delay
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviesPagingSource(private val movieApi: MovieApi) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val position = params.key ?: 1

            if (ConfigConstants.PAGING_REQUEST_DELAY > 0) {
                delay(ConfigConstants.PAGING_REQUEST_DELAY)
            }

            val currentlyPlayingResponse = movieApi
                .getCurrentlyPlaying(position)

            val movies = currentlyPlayingResponse.movieDtos
                .map {
                    try {
                        it.toMovie()
                    } catch (nullException: NullPointerException) {
                        Timber.e(nullException)
                        return LoadResult.Error(nullException)
                    }
                }

            if (currentlyPlayingResponse.movieDtos.isEmpty()) {
                return LoadResult.Error(IllegalStateException("Movie list is empty."))
            }

            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else (position - 1),
                nextKey = if (position ==
                    currentlyPlayingResponse.totalPages
                ) {
                    null
                } else {
                    position + 1
                }
            )
        } catch (httpException: HttpException) {
            LoadResult.Error(httpException)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
