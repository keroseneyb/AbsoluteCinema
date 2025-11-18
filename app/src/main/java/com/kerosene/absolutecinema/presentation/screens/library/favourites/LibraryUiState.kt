package com.kerosene.absolutecinema.presentation.screens.library.favourites

import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.presentation.screens.library.favourites.model.MovieLibraryUiModel

sealed class LibraryScreenUiState {
    object Loading : LibraryScreenUiState()

    data class Error(val message: String) : LibraryScreenUiState()

    data class Success(
        val favouriteMovies: List<MovieLibraryUiModel>,
        val notes: List<Note>,
    ) : LibraryScreenUiState()
}

enum class LibraryTab {
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