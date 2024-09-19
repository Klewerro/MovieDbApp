package com.klewerro.moviedbapp.movieDetails.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.core.util.testData.MovieTestData
import com.klewerro.moviedbapp.movieDetails.presentation.composable.MovieDetailsSection
import kotlin.math.min
import com.klewerro.moviedbapp.core.R as RCore

@Composable
fun MovieDetailsScreen(
    modifier: Modifier = Modifier,
    movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val state by movieDetailsViewModel.state.collectAsStateWithLifecycle()
    MovieDetailsScreenContent(
        state,
        modifier.fillMaxSize()
    )
}

@Composable
private fun MovieDetailsScreenContent(
    movieDetailsState: MovieDetailsState,
    modifier: Modifier = Modifier
) {
    val movie = movieDetailsState.movie
    val scrollState = rememberScrollState()
    val localConfiguration = LocalConfiguration.current
    val spacerBoxHeight by remember {
        val height = with(localConfiguration) {
            this.screenHeightDp
        }.dp
        val isInLandscape = localConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val multiplier = if (isInLandscape) 0.55f else 0.7f
        mutableStateOf(
            height * multiplier
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = movie.posterOriginalUrl,
            contentDescription = stringResource(RCore.string.movie_poster),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(min(1f, 1 - (scrollState.value / 2000f)))
                .scale(1 + scrollState.value * 0.001f)
        )
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(spacerBoxHeight))
            MovieDetailsSection(movie, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieDetailsScreenPreview() {
    val state = MovieDetailsState(
        MovieTestData.movie1,
        1
    )
    MovieDbAppTheme {
        MovieDetailsScreenContent(
            movieDetailsState = state,
            modifier = Modifier.fillMaxSize()
        )
    }
}
