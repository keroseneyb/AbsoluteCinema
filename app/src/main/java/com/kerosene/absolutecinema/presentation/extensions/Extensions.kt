package com.kerosene.absolutecinema.presentation.extensions

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.presentation.screens.home.HomeScreenUiState

fun HomeScreenUiState.Content.getMoviesForSelectedTab(): List<Movie> {
    return when (selectedTab) {
        HomeScreenUiState.Tab.POPULAR -> popularMovies
        HomeScreenUiState.Tab.ALL -> allMovies
    }
}