package com.kerosene.absolutecinema.presentation.screens.home

import com.kerosene.absolutecinema.domain.entity.Movie

sealed class HomeScreenUiState {

    object Initial : HomeScreenUiState()

    object Loading : HomeScreenUiState()

    data class Error(val message: String) : HomeScreenUiState()

    data class Content(
        val popularMovies: List<Movie>,
        val allMovies: List<Movie>,
        val selectedTab: Tab
    ) : HomeScreenUiState()

    enum class Tab {
        POPULAR,
        ALL;

        companion object {

            const val POPULAR_DISPLAY_NAME = "Популярные"
            const val ALL_DISPLAY_NAME = "Все фильмы"
        }

        val displayName: String
            get() = when (this) {
                POPULAR -> POPULAR_DISPLAY_NAME
                ALL -> ALL_DISPLAY_NAME
            }
    }
}