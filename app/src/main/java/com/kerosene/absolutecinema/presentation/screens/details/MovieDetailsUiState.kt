package com.kerosene.absolutecinema.presentation.screens.details

import com.kerosene.absolutecinema.presentation.screens.details.model.MovieDetailsUiModel

sealed interface MovieDetailsUiState {

    object Loading : MovieDetailsUiState

    data class Success(
        val movieDetails: MovieDetailsUiModel,
        val isFavourite: Boolean = false
    ) : MovieDetailsUiState

    data class Error(val message: String) : MovieDetailsUiState
}