package com.klewerro.moviedbapp.movies.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.klewerro.moviedbapp.core.domain.contract.MovieRepository
import com.klewerro.moviedbapp.core.domain.dispatcher.DispatcherProvider
import com.klewerro.moviedbapp.core.presentation.like.BaseLikeMovieViewModel
import com.klewerro.moviedbapp.core.util.ConfigConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    dispatchers: DispatcherProvider,
    private val movieRepository: MovieRepository
) : BaseLikeMovieViewModel(movieRepository, dispatchers) {

    private val _searchViewState = MutableStateFlow(SearchViewState())
    val searchViewState = _searchViewState.asStateFlow()

    val movies = searchViewState
        .map { it.searchText }
        .debounce(ConfigConstants.SEARCH_MOVIE_DEBOUNCE)
        .filter { it.isNotBlank() }
        .map { it.trim() }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)
        .catch {
            Timber.e(it)
        }.flatMapLatest {
            movieRepository.observeSearchedMovies(it, viewModelScope)
        }.cachedIn(viewModelScope)

    fun onSearchTextChange(text: String) {
        _searchViewState.update {
            it.copy(searchText = text)
        }
    }

    fun updateFirstVisibleScrollIndex(index: Int) {
        _searchViewState.update {
            it.copy(firstVisibleScrollIndex = index)
        }
    }
}
