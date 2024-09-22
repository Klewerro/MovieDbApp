package com.klewerro.moviedbapp.core.presentation.like

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.klewerro.moviedbapp.core.R

@Composable
fun LikeStateChangedLaunchedEffect(
    baseLikeMovieViewModel: BaseLikeMovieViewModel,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val likeStateChangedState by baseLikeMovieViewModel.likeStateChanged.collectAsStateWithLifecycle()

    LaunchedEffect(likeStateChangedState) {
        when (val likeStateChangedValue = likeStateChangedState) {
            LikeChanged.Unchanged -> Unit
            is LikeChanged.Liked -> {
                snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.liked_movie_message,
                        likeStateChangedValue.movieTitle
                    )
                )
                baseLikeMovieViewModel.onLikeMovieEvent(LikeMovieEvent.DismissChangedLike)
            }

            is LikeChanged.LikeRemoved -> {
                snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.disliked_movie_message,
                        likeStateChangedValue.movieTitle
                    )
                )
                baseLikeMovieViewModel.onLikeMovieEvent(LikeMovieEvent.DismissChangedLike)
            }

            is LikeChanged.Error -> {
                snackbarHostState.showSnackbar(
                    message = context.getString(
                        R.string.error_movie_liking_message,
                        likeStateChangedValue.movieTitle
                    ),
                    duration = SnackbarDuration.Long
                )
                baseLikeMovieViewModel.onLikeMovieEvent(LikeMovieEvent.DismissChangedLike)
            }
        }
    }
}
