package com.klewerro.moviedbapp.movieDetails.presentation.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.util.Locale

@Composable
fun VoteProgressIndicator(voteAverage: Double, modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        CircularProgressIndicator(
            progress = {
                voteAverage.toFloat() / 10
            },
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = String.format(Locale.US, "%.1f", voteAverage),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
