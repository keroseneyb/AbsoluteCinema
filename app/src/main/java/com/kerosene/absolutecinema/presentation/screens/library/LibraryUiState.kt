package com.kerosene.absolutecinema.presentation.screens.library

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Note

sealed class LibraryScreenUiState {

    object Loading : LibraryScreenUiState()

    data class Error(val message: String) : LibraryScreenUiState()

    data class Content(
        val favouriteMovies: List<Movie>,
        val notes: List<Note>,
        val selectedTab: Tab,
    ) : LibraryScreenUiState() {

        enum class Tab {
            FAVOURITES,
            NOTES;

            companion object {

                const val FAVOURITES_DISPLAY_NAME = "Избранное"
                const val NOTES_DISPLAY_NAME = "Заметки"
            }

            val displayName: String
                get() = when (this) {
                    FAVOURITES -> FAVOURITES_DISPLAY_NAME
                    NOTES -> NOTES_DISPLAY_NAME
                }
        }
    }
}
