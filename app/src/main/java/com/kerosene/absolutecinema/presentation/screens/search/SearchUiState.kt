package com.kerosene.absolutecinema.presentation.screens.search

import com.kerosene.absolutecinema.domain.entity.Movie

sealed class SearchUiState {

    object Initial : SearchUiState()

    object Loading : SearchUiState()

    data class Success(val movies: List<Movie>) : SearchUiState()

    data class Error(val message: String) : SearchUiState()

    object Empty : SearchUiState()
}