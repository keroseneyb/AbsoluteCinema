package com.kerosene.absolutecinema.presentation.screens.home

sealed interface HomeScreenUiState {
    object Loading : HomeScreenUiState
    data class Error(val message: String) : HomeScreenUiState
    data class Success(val selectedTab: Tab) : HomeScreenUiState
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