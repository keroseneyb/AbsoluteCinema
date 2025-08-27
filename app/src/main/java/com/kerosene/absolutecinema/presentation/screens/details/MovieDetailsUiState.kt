package com.kerosene.absolutecinema.presentation.screens.details

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.Trailer

sealed class MovieDetailsUiState {

    object Initial : MovieDetailsUiState()

    object Loading : MovieDetailsUiState()

    data class Success(
        val movie: Movie,
        val trailers: List<Trailer>,
        val reviews: List<Review>,
    ) : MovieDetailsUiState()

    data class Error(val message: String) : MovieDetailsUiState()
}