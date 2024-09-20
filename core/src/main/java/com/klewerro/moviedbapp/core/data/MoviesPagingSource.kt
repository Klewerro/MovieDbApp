package com.klewerro.moviedbapp.core.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.klewerro.moviedbapp.core.data.local.LikedMovieDao
import com.klewerro.moviedbapp.core.data.mapper.toMovie
import com.klewerro.moviedbapp.core.data.remote.MovieApi
import com.klewerro.moviedbapp.core.data.remote.dto.MovieDto
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.util.ConfigConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class MoviesPagingSource(private val movieApi: MovieApi, private val likedMovieDao: LikedMovieDao) :
    PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val position = params.key ?: 1

            if (ConfigConstants.PAGING_REQUEST_DELAY > 0) {
                delay(ConfigConstants.PAGING_REQUEST_DELAY)
            }

            val currentlyPlayingResponse = movieApi
                .getCurrentlyPlaying(position)
            val movieDtoList = currentlyPlayingResponse.movieDtos
            val likedIds = movieDtoList.getListOfLikedMovieIds()

            val movies = movieDtoList.map {
                try {
                    it.toMovie(isLiked = likedIds.contains(it.id))
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
                nextKey = if (position == currentlyPlayingResponse.totalPages) {
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

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    private suspend fun List<MovieDto>.getListOfLikedMovieIds(): List<Int> {
        val moviesIds = this.map { it.id }

        var likedIds: List<Int>
        withContext(Dispatchers.IO) {
            likedIds = likedMovieDao.getListOfLikedMovieIds(moviesIds)
        }

        return likedIds
    }
}
