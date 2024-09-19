package com.klewerro.moviedbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.klewerro.moviedbapp.core.presentation.LocalSpacing
import com.klewerro.moviedbapp.core.presentation.navigation.NavRoute
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.movieDetails.presentation.MovieDetailsScreen
import com.klewerro.moviedbapp.movies.presentation.MovieListScreen
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieDbAppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(R.string.app_name))
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            navigationIcon = {
                                // Todo: Implement after adding 2nd screen.
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val spacing = LocalSpacing.current
                    val navController = rememberNavController()
                    Box(modifier = Modifier.padding(innerPadding)) {
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
                                    modifier = Modifier.padding(horizontal = spacing.spaceScreen)
                                )
                            }
                            composable<NavRoute.MovieDetailsScreen>(
                                typeMap = NavRoute.movieDetailsScreenTypeMap
                            ) {
                                MovieDetailsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}
