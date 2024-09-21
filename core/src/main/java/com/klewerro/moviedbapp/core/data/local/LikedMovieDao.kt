package com.klewerro.moviedbapp.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LikedMovieDao {

    @Query("SELECT id FROM liked_movie")
    fun observeAllLikedMovieIds(): Flow<List<Int>>

    @Query("SELECT id FROM liked_movie WHERE id = :movieId limit 1")
    fun observeMovieLikedStatus(movieId: Int): Flow<Boolean>

    @Query("SELECT id FROM liked_movie WHERE id IN (:list)")
    suspend fun getListOfLikedMovieIds(list: List<Int>): List<Int>

    @Query("SELECT * from liked_movie WHERE id = :movieId limit 1")
    suspend fun isMovieAlreadyLiked(movieId: Int): LikedMovieEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(likedMovieEntity: LikedMovieEntity): Long

    @Delete
    suspend fun delete(likedMovieEntity: LikedMovieEntity): Int
}
