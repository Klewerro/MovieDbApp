package com.klewerro.moviedbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.klewerro.moviedbapp.core.presentation.LocalSpacing
import com.klewerro.moviedbapp.core.presentation.navigation.NavRoute
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.movieDetails.presentation.MovieDetailsScreen
import com.klewerro.moviedbapp.movies.presentation.MovieListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val spacing = LocalSpacing.current
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }

            MovieDbAppTheme {
                NavHost(
                    navController = navController,
                    startDestination = NavRoute.MovieListScreen
                ) {
                    composable<NavRoute.MovieListScreen> {
                        MovieListScreen(
                            onMovieClick = { popularityIndex, movie ->
                                navController.navigate(
                                    NavRoute.MovieDetailsScreen(
                                        popularityIndex,
                                        movie
                                    )
                                )
                            },
                            snackbarHostState = snackbarHostState,
                            modifier = Modifier.padding(horizontal = spacing.spaceScreen)
                        )
                    }
                    composable<NavRoute.MovieDetailsScreen>(
                        typeMap = NavRoute.movieDetailsScreenTypeMap
                    ) {
                        MovieDetailsScreen(
                            snackbarHostState = snackbarHostState,
                            onBackClick = navController::popBackStack
                        )
                    }
                }
            }
        }
    }
}
