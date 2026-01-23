package com.kerosene.absolutecinema.presentation.screens.library.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.domain.usecase.GetFavouriteMoviesUseCase
import com.kerosene.absolutecinema.domain.usecase.GetMovieNotesUseCase
import com.kerosene.absolutecinema.domain.usecase.ToggleFavouriteStatusUseCase
import com.kerosene.absolutecinema.presentation.screens.library.favourites.mapping.toMoviesLibraryUiModels
import com.kerosene.absolutecinema.presentation.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val getFavouriteMoviesUseCase: GetFavouriteMoviesUseCase,
    private val getMovieNotesUseCase: GetMovieNotesUseCase,
    private val toggleFavouriteStatusUseCase: ToggleFavouriteStatusUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LibraryScreenUiState>(LibraryScreenUiState.Loading)
    val uiState: StateFlow<LibraryScreenUiState> = _uiState.asStateFlow()

    private val _selectedTab = MutableStateFlow(LibraryTab.FAVOURITES)
    val selectedTab: StateFlow<LibraryTab> = _selectedTab.asStateFlow()

    init {
        loadLibraryData()
    }

    private fun favouriteMoviesFlow(): Flow<List<Movie>> = getFavouriteMoviesUseCase()

    private fun movieNotesFlow(): Flow<List<Note>> = getMovieNotesUseCase()

    private fun libraryDataFlow(): Flow<LibraryScreenUiState.Success> = combine(
        favouriteMoviesFlow(),
        movieNotesFlow()
    ) { favourites, notes ->
        LibraryScreenUiState.Success(
            favouriteMovies = favourites.toMoviesLibraryUiModels(),
            notes = notes
        )
    }

    private fun loadLibraryData() {
        libraryDataFlow()
            .onStart { _uiState.update { LibraryScreenUiState.Loading } }
            .onEach { successState ->
                _uiState.update { successState }
            }
            .catch { e ->
                _uiState.update {
                    LibraryScreenUiState.Error(e.message ?: Constants.UNKNOWN_ERROR)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onTabSelected(tab: LibraryTab) {
        _selectedTab.update { tab }
    }

    fun toggleFavourite(movieId: Int, title: String) {
        viewModelScope.launch {
            toggleFavouriteStatusUseCase(movieId, title)
        }
    }
}