package com.klewerro.moviedbapp.movies.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.klewerro.moviedbapp.core.R
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.presentation.LocalSpacing
import com.klewerro.moviedbapp.core.presentation.MovieAppBar
import com.klewerro.moviedbapp.core.presentation.like.LikeMovieEvent
import com.klewerro.moviedbapp.core.presentation.like.LikeStateChangedLaunchedEffect
import com.klewerro.moviedbapp.movies.presentation.composable.MovieListScreenContent

@Composable
fun MovieListScreen(
    onMovieClick: (Int, Movie) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    movieListViewModel: MovieListViewModel = hiltViewModel<MovieListViewModel>(),
    searchViewModel: SearchViewModel = hiltViewModel<SearchViewModel>()
) {
    val spacing = LocalSpacing.current
    val searchViewState by searchViewModel.searchViewState.collectAsStateWithLifecycle()
    val searchedMovies = searchViewModel.movies.collectAsLazyPagingItems()
    var isSearching by rememberSaveable {
        mutableStateOf(false)
    }
    var skipFirstRefreshIndication by rememberSaveable {
        mutableStateOf(true)
    }

    Scaffold(
        topBar = {
            MovieAppBar(
                screenTitle = stringResource(R.string.app_name),
                isNavBackPossible = false

            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isSearching = !isSearching
                },
                modifier = Modifier.padding(end = spacing.spaceNormal)
            ) {
                Icon(
                    imageVector = if (isSearching) {
                        Icons.AutoMirrored.Filled.List
                    } else {
                        Icons.Default.Search
                    },
                    contentDescription = if (isSearching) {
                        stringResource(R.string.list_of_now_playing)
                    } else {
                        stringResource(R.string.search)
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (isSearching) {
                LaunchedEffect(searchedMovies.loadState.refresh) {
                    if (searchedMovies.loadState.refresh != LoadState.Loading) {
                        skipFirstRefreshIndication = false
                    }
                }
                LikeStateChangedLaunchedEffect(searchViewModel, snackbarHostState)

                Column(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    TextField(
                        value = searchViewState.searchText,
                        onValueChange = searchViewModel::onSearchTextChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true

                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MovieListScreenContent(
                        moviesPager = searchedMovies,
                        onMovieClick = onMovieClick,
                        onMovieLongClick = {
                            searchViewModel.onLikeMovieEvent(LikeMovieEvent.LikeMovie(it))
                        },
                        skipRefreshIndication = skipFirstRefreshIndication,
                        modifier = Modifier.weight(1f),
                        initialFirstVisibleItemIndex = searchViewState.firstVisibleScrollIndex,
                        onDisposeFirstVisibleItemIndex = {
                            searchViewModel.updateFirstVisibleScrollIndex(it)
                        }
                    )
                }
            } else {
                val currentlyPlayingMovies =
                    movieListViewModel.currentlyPlayingMoviesPages.collectAsLazyPagingItems()
                val currentlyPlayingFirstVisibleScrollIndex by movieListViewModel.firstVisibleScrollIndex
                    .collectAsStateWithLifecycle()

                LikeStateChangedLaunchedEffect(movieListViewModel, snackbarHostState)

                MovieListScreenContent(
                    moviesPager = currentlyPlayingMovies,
                    onMovieClick = onMovieClick,
                    onMovieLongClick = {
                        movieListViewModel.onLikeMovieEvent(LikeMovieEvent.LikeMovie(it))
                    },
                    modifier = modifier
                        .fillMaxSize(),
                    initialFirstVisibleItemIndex = currentlyPlayingFirstVisibleScrollIndex,
                    onDisposeFirstVisibleItemIndex = {
                        movieListViewModel.updateFirstVisibleScrollIndex(it)
                    }
                )
            }
        }
    }
}
