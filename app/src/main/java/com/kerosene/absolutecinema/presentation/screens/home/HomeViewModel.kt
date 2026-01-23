package com.kerosene.absolutecinema.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kerosene.absolutecinema.domain.usecase.GetMoviesPagedUseCase
import com.kerosene.absolutecinema.domain.usecase.GetPopularPagedMoviesUseCase
import com.kerosene.absolutecinema.presentation.screens.home.mapping.toMoviePreviewUiModel
import com.kerosene.absolutecinema.presentation.screens.home.model.MoviePreviewUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularPagedMoviesUseCase: GetPopularPagedMoviesUseCase,
    private val getMoviesPagedUseCase: GetMoviesPagedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState

    val allMoviesFlow: Flow<PagingData<MoviePreviewUiModel>> =
        getMoviesPagedUseCase()
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map {
                    it.toMoviePreviewUiModel()
                }
            }

    val popularMoviesFlow: Flow<PagingData<MoviePreviewUiModel>> =
        getPopularPagedMoviesUseCase()
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { movie ->
                    movie.toMoviePreviewUiModel()
                }
            }

    init {
        _uiState.update { HomeScreenUiState.Success(Tab.POPULAR) }
    }

    fun onTabSelected(tab: Tab) {
        _uiState.update { current ->
            if (current is HomeScreenUiState.Success) current.copy(selectedTab = tab)
            else current
        }
    }
}
