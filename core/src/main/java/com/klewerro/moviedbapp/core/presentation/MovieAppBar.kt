@file:OptIn(ExperimentalMaterial3Api::class)

package com.klewerro.moviedbapp.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.klewerro.moviedbapp.core.R
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.core.R as RCore

@Composable
fun MovieAppBar(
    screenTitle: String,
    isNavBackPossible: Boolean,
    onBackArrowClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
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
            AnimatedVisibility(isNavBackPossible) {
                IconButton(onClick = onBackArrowClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(
                            RCore.string.navigate_back
                        ),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = actions

    )
}

@PreviewLightDark
@Composable
private fun MovieAppBarPreview() {
    MovieDbAppTheme {
        MovieAppBar(
            screenTitle = "Screen title",
            isNavBackPossible = false
        )
    }
}

@PreviewLightDark
@Composable
private fun MovieAppBarWithBackArrowPreview() {
    MovieDbAppTheme {
        MovieAppBar(
            screenTitle = "Screen title",
            isNavBackPossible = true
        )
    }
}

@PreviewLightDark
@Composable
private fun MovieAppBarMovieDetailsMovieLikedPreview() {
    MovieDbAppTheme {
        MovieAppBar(
            screenTitle = "Screen title",
            isNavBackPossible = true,
            actions = {
                Icon(imageVector = Icons.Default.Star, contentDescription = null)
                Icon(imageVector = Icons.Default.CarCrash, contentDescription = null)
            }
        )
    }
}
