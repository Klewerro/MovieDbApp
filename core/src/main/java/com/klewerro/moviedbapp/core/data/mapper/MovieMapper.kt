package com.klewerro.moviedbapp.core.data.mapper

import com.klewerro.moviedbapp.core.data.remote.dto.MovieDto
import com.klewerro.moviedbapp.core.domain.Movie

fun MovieDto.toMovie(isLiked: Boolean) = Movie(
    id = this.id,
    title = this.title,
    originalTitle = this.originalTitle,
    popularity = this.popularity,
    backdropPath = this.backdropPath,
    posterPath = this.posterPath,
    adult = this.adult,
    overview = this.overview,
    genreIds = this.genreIds,
    originalLanguage = this.originalLanguage,
    releaseDate = this.releaseDate,
    video = this.video,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount,
    isLiked = isLiked
)
