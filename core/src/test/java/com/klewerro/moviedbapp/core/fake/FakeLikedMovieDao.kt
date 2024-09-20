package com.klewerro.moviedbapp.core.fake

import com.klewerro.moviedbapp.core.data.local.LikedMovieDao
import com.klewerro.moviedbapp.core.data.local.LikedMovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeLikedMovieDao : LikedMovieDao {

    var savedLikedMovies = MutableStateFlow(
        listOf(
            1,
            2,
            3,
            4,
            5,
            6,
            8,
            10
        )
    )
    var isError = false

    override fun observeAllLikedMovieIds(): Flow<List<Int>> = savedLikedMovies

    override fun observeMovieLikedStatus(movieId: Int): Flow<Boolean> =
        savedLikedMovies.map { list ->
            list.any { it == movieId }
        }

    override suspend fun getListOfLikedMovieIds(list: List<Int>): List<Int> = savedLikedMovies.value

    override suspend fun isMovieAlreadyLiked(movieId: Int): LikedMovieEntity? =
        savedLikedMovies.value.firstOrNull {
            it == movieId
        }?.let {
            LikedMovieEntity(it, -1L)
        } ?: run {
            null
        }

    override suspend fun add(likedMovieEntity: LikedMovieEntity): Long {
        if (isError) return -1
        savedLikedMovies.update { it.plus(likedMovieEntity.id) }
        return likedMovieEntity.id.toLong()
    }

    override suspend fun delete(likedMovieEntity: LikedMovieEntity): Int {
        if (isError) return -1
        savedLikedMovies.update {
            it.minus(likedMovieEntity.id)
        }
        return 1
    }
}
