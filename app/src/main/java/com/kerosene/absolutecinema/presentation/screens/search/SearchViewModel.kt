package com.kerosene.absolutecinema.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.usecase.SearchMovieUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    fun searchMovies() {
        val currentQuery = _query.value
        if (currentQuery.isBlank()) return

        _uiState.value = SearchUiState.Loading

        viewModelScope.launch {
            try {
                val result = searchMovieUseCase(currentQuery)
                _uiState.value = SearchUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    companion object {

        private const val UNKNOWN_ERROR = "Unknown error"
    }
}