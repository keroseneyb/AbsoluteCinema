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
import com.kerosene.absolutecinema.presentation.utils.handleApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        viewModelScope.launch {
            handleApiCall(
                apiCall = {
                    libraryDataFlow().first()
                },
                onLoading = {
                    _uiState.value = LibraryScreenUiState.Loading
                },
                onSuccess = { successState ->
                    _uiState.value = successState
                    observeLibraryUpdates()
                },
                onError = { message ->
                    _uiState.value = LibraryScreenUiState.Error(message)
                }
            )
        }
    }

    private fun observeLibraryUpdates() {
        viewModelScope.launch {
            libraryDataFlow()
                .catch { e ->
                    _uiState.value =
                        LibraryScreenUiState.Error(e.message ?: Constants.UNKNOWN_ERROR)
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }
    }

    fun onTabSelected(tab: LibraryTab) {
        _selectedTab.value = tab
    }

    fun toggleFavourite(movieId: Int, title: String) {
        viewModelScope.launch {
            toggleFavouriteStatusUseCase(movieId, title)
        }
    }
}