package com.klewerro.moviedbapp.movieDetails.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = movieDetailsViewModel.movie.title,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
