package com.kerosene.absolutecinema.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.usecase.GetMoviesUseCase
import com.kerosene.absolutecinema.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Initial)
    val uiState: StateFlow<HomeScreenUiState> = _uiState


    fun loadMovies() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUiState.Initial
            _uiState.value = HomeScreenUiState.Loading
            try {

                val popularMovies = getPopularMoviesUseCase()
                val allMovies = getMoviesUseCase()

                _uiState.value = HomeScreenUiState.Content(
                    popularMovies = popularMovies,
                    allMovies = allMovies,
                    selectedTab = HomeScreenUiState.Tab.POPULAR
                )
            } catch (e: Exception) {
                _uiState.value = HomeScreenUiState.Error(e.message ?: LOAD_ERROR)
            }
        }
    }

    fun onTabSelected(tab: HomeScreenUiState.Tab) {
        _uiState.update { currentState ->
            if (currentState is HomeScreenUiState.Content) {
                currentState.copy(selectedTab = tab)
            } else {
                currentState
            }
        }
    }

    companion object {

        private const val LOAD_ERROR = "Error loading data"
    }
}
