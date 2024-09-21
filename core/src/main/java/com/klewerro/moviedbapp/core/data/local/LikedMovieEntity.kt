package com.klewerro.moviedbapp.core.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_movie")
data class LikedMovieEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("date_time")
    val dateTime: Long
)
