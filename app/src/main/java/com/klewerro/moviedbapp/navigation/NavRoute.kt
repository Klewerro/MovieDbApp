package com.klewerro.moviedbapp.navigation

import com.klewerro.moviedbapp.core.domain.Movie
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

object NavRoute {

    val movieDetailsScreenTypeMap = mapOf(typeOf<Movie>() to CustomNavType.UserType)

    @Serializable
    object MovieListScreen

    /**
     * Index is used to represent popularity list place.
     */
    @Serializable
    data class MovieDetailsScreen(val index: Int, val movie: Movie)
}
