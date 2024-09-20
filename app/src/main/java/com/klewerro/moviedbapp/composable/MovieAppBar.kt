@file:OptIn(ExperimentalMaterial3Api::class)

package com.klewerro.moviedbapp.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.klewerro.moviedbapp.R
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.presentation.navigation.NavRoute
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.core.ui.theme.gold
import com.klewerro.moviedbapp.core.ui.theme.goldDark

@Composable
fun MovieAppBar(
    backStackEntry: NavBackStackEntry?,
    onBackArrowClick: () -> Unit,
    onLikeIconClick: (Movie) -> Unit,
    isMovieLiked: Boolean?
) {
    val routeName = backStackEntry
        ?.destination
        ?.route
        ?.substringAfterLast(".")
        ?.substringBefore("/")
    val isMovieDetailsScreen = when (routeName) {
        "MovieDetailsScreen" -> true
        else -> false
    }
    MovieAppBarContent(
        isMovieDetailsScreen = isMovieDetailsScreen,
        isMovieLiked = isMovieLiked,
        onBackArrowClick = onBackArrowClick,
        onLikeIconClick = {
            val movieDetailsScreenRoute = backStackEntry
                ?.toRoute<NavRoute.MovieDetailsScreen>()
            movieDetailsScreenRoute?.movie?.let {
                onLikeIconClick(it)
            }
        }
    )
}

@Composable
private fun MovieAppBarContent(
    isMovieDetailsScreen: Boolean,
    isMovieLiked: Boolean?,
    onBackArrowClick: () -> Unit,
    onLikeIconClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        navigationIcon = {
            AnimatedVisibility(isMovieDetailsScreen) {
                IconButton(onClick = onBackArrowClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            com.klewerro.moviedbapp.core.R.string.navigate_back
                        ),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            if (isMovieDetailsScreen) {
                AnimatedVisibility(
                    visible = isMovieLiked != null,
                    enter = expandIn(),
                    exit = shrinkOut()
                ) {
                    isMovieLiked?.let { isLiked ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = stringResource(
                                com.klewerro.moviedbapp.core.R.string.filled_star_content_description
                            ),
                            tint = if (isLiked) {
                                if (isSystemInDarkTheme()) gold else goldDark
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .clickable {
                                    onLikeIconClick()
                                }
                        )
                    }
                }
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun MovieAppBarPreview() {
    MovieDbAppTheme {
        MovieAppBarContent(
            isMovieDetailsScreen = false,
            isMovieLiked = false,
            onBackArrowClick = {},
            onLikeIconClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun MovieAppBarMovieDetailsPreview() {
    MovieDbAppTheme {
        MovieAppBarContent(
            isMovieDetailsScreen = true,
            isMovieLiked = false,
            onBackArrowClick = {},
            onLikeIconClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun MovieAppBarMovieDetailsMovieLikedPreview() {
    MovieDbAppTheme {
        MovieAppBarContent(
            isMovieDetailsScreen = true,
            isMovieLiked = true,
            onBackArrowClick = {},
            onLikeIconClick = {}
        )
    }
}
