package com.kerosene.absolutecinema.presentation.screens.home

import com.kerosene.absolutecinema.presentation.screens.home.model.MoviePreviewUiModel

sealed class HomeScreenUiState {
    object Loading : HomeScreenUiState()
    data class Error(val message: String) : HomeScreenUiState()
    data class Success(val tabs: TabsUiState) : HomeScreenUiState()
}

data class TabsUiState(
    val selectedTab: Tab,
    val popularMovies: List<MoviePreviewUiModel>,
    val allMovies: List<MoviePreviewUiModel>,
) {
    fun moviesFor(tab: Tab = selectedTab): List<MoviePreviewUiModel> =
        when (tab) {
            Tab.POPULAR -> popularMovies
            Tab.ALL -> allMovies
        }
}

enum class Tab {
    POPULAR,
    ALL;

    val displayName: String
        get() = when (this) {
            POPULAR -> POPULAR_DISPLAY_NAME
            ALL -> ALL_DISPLAY_NAME
        }

    companion object {
        const val POPULAR_DISPLAY_NAME = "Популярные"
        const val ALL_DISPLAY_NAME = "Все фильмы"
    }
}