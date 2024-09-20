package com.klewerro.moviedbapp.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.klewerro.moviedbapp.core.data.local.LikedMovieDao
import com.klewerro.moviedbapp.core.data.local.LikedMovieEntity
import com.klewerro.moviedbapp.core.data.remote.MovieApi
import com.klewerro.moviedbapp.core.domain.LikeResult
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.domain.contract.DateTimeProvider
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.core.util.ConfigConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val likedMovieDao: LikedMovieDao,
    private val dateTimeProvider: DateTimeProvider
) : MovieRepository {

    override fun observeCurrentlyPlayingMoviesPages(scope: CoroutineScope) = Pager(
        config = PagingConfig(
            pageSize = ConfigConstants.PAGING_CONFIG_PAGE_SIZE,
            prefetchDistance = 1,
            initialLoadSize = 1,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { MoviesPagingSource(movieApi, likedMovieDao) }
    ).flow.cachedIn(scope)
        .combine(likedMovieDao.observeAllLikedMovieIds()) { moviePagingData, likedMovies ->
            moviePagingData.map { movie ->
                movie.copy(
                    isLiked = likedMovies.contains(movie.id)
                )
            }
        }
        .cachedIn(scope)

    override fun observeMovieLikedStatus(movieId: Int): Flow<Boolean> =
        likedMovieDao.observeMovieLikedStatus(movieId)

    override suspend fun likeMovie(movie: Movie): LikeResult {
        val movieWithId = likedMovieDao.isMovieAlreadyLiked(movie.id)

        return if (movieWithId == null) {
            val entity = LikedMovieEntity(
                id = movie.id,
                dateTime = dateTimeProvider.currentTimeInMillis()
            )
            if (likedMovieDao.add(entity) > 0) {
                LikeResult.LIKED
            } else {
                LikeResult.ERROR
            }
        } else {
            if (likedMovieDao.delete(movieWithId) > 0) {
                LikeResult.LIKE_REMOVED
            } else {
                LikeResult.ERROR
            }
        }
    }
}
