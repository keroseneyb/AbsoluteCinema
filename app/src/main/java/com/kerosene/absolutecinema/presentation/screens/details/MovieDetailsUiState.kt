package com.kerosene.absolutecinema.presentation.screens.details

import com.kerosene.absolutecinema.presentation.screens.details.model.MovieDetailsUiModel

sealed class MovieDetailsUiState {

    object Loading : MovieDetailsUiState()

    data class Success(val movieDetails: MovieDetailsUiModel) : MovieDetailsUiState()

    data class Error(val message: String) : MovieDetailsUiState()
}