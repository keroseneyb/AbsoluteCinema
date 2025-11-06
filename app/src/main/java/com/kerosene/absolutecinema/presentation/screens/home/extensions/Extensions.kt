package com.kerosene.absolutecinema.presentation.screens.home.extensions

import com.kerosene.absolutecinema.presentation.screens.home.HomeScreenUiState
import com.kerosene.absolutecinema.presentation.screens.home.model.MoviePreviewUiModel

fun HomeScreenUiState.Content.getMoviesForSelectedTab(): List<MoviePreviewUiModel> {
    return when (selectedTab) {
        HomeScreenUiState.Tab.POPULAR -> popularMovies
        HomeScreenUiState.Tab.ALL -> allMovies
    }
}