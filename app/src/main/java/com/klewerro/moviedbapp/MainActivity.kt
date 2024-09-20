package com.klewerro.moviedbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.klewerro.moviedbapp.composable.MovieAppBar
import com.klewerro.moviedbapp.core.presentation.LocalSpacing
import com.klewerro.moviedbapp.core.presentation.navigation.NavRoute
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.movieDetails.presentation.MovieDetailsScreen
import com.klewerro.moviedbapp.movies.presentation.MovieListScreen
import com.klewerro.moviedbapp.presentation.LikeChanged
import com.klewerro.moviedbapp.presentation.LikeMovieViewModel
import com.klewerro.moviedbapp.presentation.like.LikeMovieEvent
import dagger.hilt.android.AndroidEntryPoint
import com.klewerro.moviedbapp.core.R as RCore

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val spacing = LocalSpacing.current
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            val likeMovieViewModel: LikeMovieViewModel = hiltViewModel()
            val likedMovieState by likeMovieViewModel.likeMovieState.collectAsStateWithLifecycle()
            MovieDbAppTheme {
                LaunchedEffect(likedMovieState.likeChanged) {
                    when (val changedObject = likedMovieState.likeChanged) {
                        LikeChanged.Unchanged -> Unit
                        is LikeChanged.Liked -> {
                            snackbarHostState.showSnackbar(
                                context.getString(
                                    RCore.string.liked_movie_message,
                                    changedObject.movieTitle
                                )
                            )
                            likeMovieViewModel.onEvent(LikeMovieEvent.DismissLikeChanged)
                        }

                        is LikeChanged.LikeRemoved -> {
                            snackbarHostState.showSnackbar(
                                context.getString(
                                    RCore.string.disliked_movie_message,
                                    changedObject.movieTitle
                                )
                            )
                            likeMovieViewModel.onEvent(LikeMovieEvent.DismissLikeChanged)
                        }

                        is LikeChanged.Error -> {
                            snackbarHostState.showSnackbar(
                                message = context.getString(
                                    RCore.string.error_movie_liking_message,
                                    changedObject.movieTitle
                                ),
                                duration = SnackbarDuration.Long
                            )
                            likeMovieViewModel.onEvent(LikeMovieEvent.DismissLikeChanged)
                        }
                    }
                }

                Scaffold(
                    topBar = {
                        MovieAppBar(
                            backStackEntry = backStackEntry,
                            isMovieLiked = likedMovieState.isMovieLiked,
                            onBackArrowClick = navController::popBackStack,
                            onLikeIconClick = {
                                likeMovieViewModel.onEvent(
                                    LikeMovieEvent.LikeMovie(it)
                                )
                            }
                        )
                    },
                    snackbarHost = {
                        SnackbarHost(snackbarHostState)
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = NavRoute.MovieListScreen
                        ) {
                            composable<NavRoute.MovieListScreen> {
                                likeMovieViewModel.onEvent(
                                    LikeMovieEvent.StopObservingMovieLikeStatus
                                )
                                MovieListScreen(
                                    onMovieClick = { popularityIndex, movie ->
                                        navController.navigate(
                                            NavRoute.MovieDetailsScreen(
                                                popularityIndex,
                                                movie
                                            )
                                        )
                                    },
                                    onMovieLongClick = {
                                        likeMovieViewModel.onEvent(
                                            LikeMovieEvent.LikeMovie(it)
                                        )
                                    },
                                    modifier = Modifier.padding(horizontal = spacing.spaceScreen)
                                )
                            }
                            composable<NavRoute.MovieDetailsScreen>(
                                typeMap = NavRoute.movieDetailsScreenTypeMap
                            ) { backStackEntry ->
                                val movieId = backStackEntry
                                    .toRoute<NavRoute.MovieDetailsScreen>()
                                    .movie.id
                                likeMovieViewModel.onEvent(
                                    LikeMovieEvent.ObserveMovieLikeStatus(movieId)
                                )
                                MovieDetailsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppBarPreview() {
    MovieDbAppTheme {
    }
}
