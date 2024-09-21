package com.klewerro.moviedbapp.movies.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.movies.presentation.composable.MovieListScreenContent

@Composable
fun MovieListScreen(
    onMovieClick: (Int, Movie) -> Unit,
    onMovieLongClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    isSearching: Boolean,
    movieListViewModel: MovieListViewModel = hiltViewModel<MovieListViewModel>(),
    searchViewModel: SearchViewModel = hiltViewModel<SearchViewModel>()
) {
    val searchViewState by searchViewModel.searchViewState.collectAsStateWithLifecycle()
    val searchedMovies = searchViewModel.movies.collectAsLazyPagingItems()
    var skipFirstRefreshIndication by rememberSaveable {
        mutableStateOf(true)
    }

    if (isSearching) {
        LaunchedEffect(searchedMovies.loadState.refresh) {
            if (searchedMovies.loadState.refresh != LoadState.Loading) {
                skipFirstRefreshIndication = false
            }
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(0.dp)
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
                onMovieLongClick = onMovieLongClick,
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
        MovieListScreenContent(
            moviesPager = currentlyPlayingMovies,
            onMovieClick = onMovieClick,
            onMovieLongClick = onMovieLongClick,
            modifier = modifier.fillMaxSize(),
            initialFirstVisibleItemIndex = currentlyPlayingFirstVisibleScrollIndex,
            onDisposeFirstVisibleItemIndex = {
                movieListViewModel.updateFirstVisibleScrollIndex(it)
            }
        )
    }
}
