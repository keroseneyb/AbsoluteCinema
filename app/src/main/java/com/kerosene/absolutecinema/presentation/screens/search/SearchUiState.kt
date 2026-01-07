package com.kerosene.absolutecinema.presentation.screens.search

import com.kerosene.absolutecinema.presentation.screens.search.model.MovieSearchUiModel

sealed interface SearchUiState {

    object Initial : SearchUiState

    object Loading : SearchUiState

    data class Success(val movies: List<MovieSearchUiModel>) : SearchUiState

    data class Error(val message: String) : SearchUiState

    object Empty : SearchUiState
}