package com.klewerro.moviedbapp.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        LikedMovieEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract val likedMovieDao: LikedMovieDao
}
