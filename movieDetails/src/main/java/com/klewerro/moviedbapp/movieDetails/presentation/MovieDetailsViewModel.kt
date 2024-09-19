package com.klewerro.moviedbapp.movieDetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.klewerro.moviedbapp.core.presentation.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(savedState: SavedStateHandle) : ViewModel() {

    private val movieDetailsScreenParams = savedState.toRoute<NavRoute.MovieDetailsScreen>(
        typeMap = NavRoute.movieDetailsScreenTypeMap
    )
    val movie = movieDetailsScreenParams.movie
}
