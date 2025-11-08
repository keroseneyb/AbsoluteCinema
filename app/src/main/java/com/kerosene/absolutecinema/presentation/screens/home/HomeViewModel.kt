package com.kerosene.absolutecinema.presentation.screens.home

import android.R.id.tabs
import android.util.Log.e
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.usecase.GetMoviesUseCase
import com.kerosene.absolutecinema.domain.usecase.GetPopularMoviesUseCase
import com.kerosene.absolutecinema.presentation.screens.home.mapping.toMoviePreviewUiModels
import com.kerosene.absolutecinema.presentation.utils.handleApiCall
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState

    init {
        viewModelScope.launch {
            loadMovies()
        }
    }

    suspend fun loadMovies() {
        handleApiCall(
            apiCall = {
                supervisorScope {
                    val popularDeferred = async { getPopularMoviesUseCase().toMoviePreviewUiModels() }
                    val allDeferred = async { getMoviesUseCase().toMoviePreviewUiModels() }

                    val popularMovies = popularDeferred.await()
                    val allMovies = allDeferred.await()

                    TabsUiState(
                        selectedTab = Tab.POPULAR,
                        popularMovies = popularMovies,
                        allMovies = allMovies
                    )
                }
            },
            onSuccess = { tabs ->
                _uiState.value = HomeScreenUiState.Success(tabs)
            },
            onError = { message ->
                _uiState.value = HomeScreenUiState.Error(message)
            }
        )
    }

    fun onTabSelected(tab: Tab) {
        _uiState.update { currentState ->
            if (currentState is HomeScreenUiState.Success) {
                currentState.copy(
                    tabs = currentState.tabs.copy(selectedTab = tab)
                )
            } else {
                currentState
            }
        }
    }
}
