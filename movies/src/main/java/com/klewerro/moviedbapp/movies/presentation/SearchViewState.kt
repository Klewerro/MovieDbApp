package com.klewerro.moviedbapp.movies.presentation

import com.klewerro.moviedbapp.core.presentation.like.LikeChanged

data class SearchViewState(
    val searchText: String = "",
    val firstVisibleScrollIndex: Int = 0,
    val likeChanged: LikeChanged = LikeChanged.Unchanged
)
