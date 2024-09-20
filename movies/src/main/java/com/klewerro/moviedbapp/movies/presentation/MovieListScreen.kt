package com.klewerro.moviedbapp.movies.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.klewerro.moviedbapp.core.domain.Movie
import com.klewerro.moviedbapp.core.presentation.LocalSpacing
import com.klewerro.moviedbapp.core.ui.theme.MovieDbAppTheme
import com.klewerro.moviedbapp.core.util.testData.MovieTestData
import com.klewerro.moviedbapp.movies.presentation.composable.ErrorMessageRefreshCard
import com.klewerro.moviedbapp.movies.presentation.composable.MovieListItem
import kotlinx.coroutines.flow.flowOf

@Composable
fun MovieListScreen(
    onMovieClick: (Int, Movie) -> Unit,
    onMovieLongClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    movieListViewModel: MovieListViewModel = hiltViewModel<MovieListViewModel>()
) {
    val currentlyPlayingMovies =
        movieListViewModel.currentlyPlayingMoviesPages.collectAsLazyPagingItems()
    MovieListScreenContent(
        moviesPager = currentlyPlayingMovies,
        onMovieClick = onMovieClick,
        onMovieLongClick = onMovieLongClick,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun MovieListScreenContent(
    moviesPager: LazyPagingItems<Movie>,
    modifier: Modifier = Modifier,
    onMovieClick: (Int, Movie) -> Unit,
    onMovieLongClick: (Movie) -> Unit
) {
    val spacing = LocalSpacing.current

    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(180.dp),
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
        ) {
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                Spacer(modifier = Modifier.padding(top = spacing.spaceScreen))
            }
            items(
                moviesPager.itemCount
            ) { index ->
                val movie = moviesPager[index]
                movie?.let { movieValue ->
                    MovieListItem(
                        movieValue,
                        onMovieClick = {
                            onMovieClick(index, movieValue)
                        },
                        onMovieLongClick = {
                            onMovieLongClick(movieValue)
                        },
                        modifier = Modifier.size(180.dp, 270.dp)
                    )
                }
            }
            item(span = {
                GridItemSpan(this.maxLineSpan)
            }) {
                MoviePaginationProgressAndErrorHandling(
                    moviesPager,
                    this
                )
            }
        }
    }
}

@Composable
private fun MoviePaginationProgressAndErrorHandling(
    moviesPager: LazyPagingItems<Movie>,
    lazyGridItemScope: LazyGridItemScope
) {
    val spacing = LocalSpacing.current

    with(lazyGridItemScope) {
        when (moviesPager.loadState.append) {
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                ErrorMessageRefreshCard(
                    errorLoadState = moviesPager.loadState.append,
                    onRetryClick = { moviesPager.retry() },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(spacing.spaceLarge))
            }
        }
        when (moviesPager.loadState.refresh) {
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorMessageRefreshCard(
                        errorLoadState = moviesPager.loadState.refresh,
                        onRetryClick = { moviesPager.retry() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// region preview
@Preview(
    name = "Landscape",
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Preview(showBackground = true)
@Composable
private fun MovieListScreenContentPreview() {
    val pagingFlow = flowOf(
        PagingData.from(
            data = listOf(
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()
    MovieDbAppTheme {
        MovieListScreenContent(
            moviesPager = pagingFlow,
            onMovieClick = { _, _ -> },
            onMovieLongClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListScreenContentRefreshPreview() {
    val pagingFlow = flowOf(
        PagingData.from(
            data = emptyList<Movie>(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.Loading,
                append = LoadState.NotLoading(false),
                prepend = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()
    MovieDbAppTheme {
        MovieListScreenContent(
            moviesPager = pagingFlow,
            onMovieClick = { _, _ -> },
            onMovieLongClick = {},
            modifier = Modifier.height(200.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieListScreenContentAppendPreview() {
    val pagingFlow = flowOf(
        PagingData.from(
            data = listOf(
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                append = LoadState.Loading,
                prepend = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()
    MovieDbAppTheme {
        MovieListScreenContent(
            moviesPager = pagingFlow,
            onMovieClick = { _, _ -> },
            onMovieLongClick = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun MovieListScreenContentAppendErrorPreview() {
    val pagingFlow = flowOf(
        PagingData.from(
            data = listOf(
                MovieTestData.movie1,
                MovieTestData.movie2,
                MovieTestData.movie1,
                MovieTestData.movie2
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(false),
                append = LoadState.Error(Exception("Exception message")),
                prepend = LoadState.NotLoading(false)
            )
        )
    ).collectAsLazyPagingItems()
    MovieDbAppTheme {
        MovieListScreenContent(
            moviesPager = pagingFlow,
            onMovieClick = { _, _ -> },
            onMovieLongClick = {}
        )
    }
}
// endregion
