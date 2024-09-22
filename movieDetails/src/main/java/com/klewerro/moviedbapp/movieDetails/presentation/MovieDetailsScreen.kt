package com.klewerro.moviedbapp.movieDetails.presentation

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.klewerro.moviedbapp.core.R
import com.klewerro.moviedbapp.core.presentation.MovieAppBar
import com.klewerro.moviedbapp.core.presentation.like.LikeMovieEvent
import com.klewerro.moviedbapp.core.presentation.like.LikeStateChangedLaunchedEffect
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.core.ui.theme.gold
import com.klewerro.moviedbapp.core.ui.theme.goldDark
import com.klewerro.moviedbapp.core.util.testData.MovieTestData
import com.klewerro.moviedbapp.movieDetails.presentation.composable.MovieDetailsSection
import kotlin.math.min
import com.klewerro.moviedbapp.core.R as RCore

@Composable
fun MovieDetailsScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    movieDetailsViewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val state by movieDetailsViewModel.state.collectAsStateWithLifecycle()

    LikeStateChangedLaunchedEffect(movieDetailsViewModel, snackbarHostState)

    MovieDetailsScreenContent(
        movieDetailsState = state,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onStarClick = {
            movieDetailsViewModel.onLikeMovieEvent(LikeMovieEvent.LikeMovie(state.movie))
        },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun MovieDetailsScreenContent(
    movieDetailsState: MovieDetailsState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onStarClick: () -> Unit,
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

    Scaffold(
        topBar = {
            MovieAppBar(
                screenTitle = stringResource(R.string.app_name),
                isNavBackPossible = true,
                onBackArrowClick = onBackClick,
                actions = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(
                            RCore.string.filled_star_content_description
                        ),
                        tint = if (movieDetailsState.movie.isLiked) {
                            if (isSystemInDarkTheme()) gold else goldDark
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                onStarClick()
                            }
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(spacerBoxHeight)
                )
                MovieDetailsSection(movie, modifier = Modifier.fillMaxWidth())
            }
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
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onStarClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
