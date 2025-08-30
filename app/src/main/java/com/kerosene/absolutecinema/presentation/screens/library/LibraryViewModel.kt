package com.kerosene.absolutecinema.presentation.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.usecase.GetFavouriteMoviesUseCase
import com.kerosene.absolutecinema.domain.usecase.GetMovieNotesUseCase
import com.kerosene.absolutecinema.domain.usecase.ToggleFavouriteStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class LibraryViewModel @Inject constructor(
    private val getFavouriteMoviesUseCase: GetFavouriteMoviesUseCase,
    private val getMovieNotesUseCase: GetMovieNotesUseCase,
    private val toggleFavouriteStatusUseCase: ToggleFavouriteStatusUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibraryScreenUiState>(LibraryScreenUiState.Loading)
    val uiState: StateFlow<LibraryScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getFavouriteMoviesUseCase(),
                getMovieNotesUseCase()
            ) { favourites, notes ->
                LibraryScreenUiState.Content(
                    favouriteMovies = favourites,
                    notes = notes,
                    selectedTab = (_uiState.value as? LibraryScreenUiState.Content)?.selectedTab
                        ?: LibraryScreenUiState.Content.Tab.FAVOURITES
                )
            }
                .catch { e ->
                    _uiState.value = LibraryScreenUiState.Error(e.message ?: LOAD_ERROR)
                }
                .collect { _uiState.value = it }
        }
    }

    fun onTabSelected(tab: LibraryScreenUiState.Content.Tab) {
        _uiState.update { current ->
            if (current is LibraryScreenUiState.Content) current.copy(selectedTab = tab)
            else current
        }
    }

    fun toggleFavourite(movie: Movie) {
        viewModelScope.launch {
            toggleFavouriteStatusUseCase(movie)
        }
    }

    companion object {

        private const val LOAD_ERROR = "Ошибка загрузки"
    }
}