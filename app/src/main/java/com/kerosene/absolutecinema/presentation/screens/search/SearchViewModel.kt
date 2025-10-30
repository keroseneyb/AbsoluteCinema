package com.kerosene.absolutecinema.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.entity.Movie
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

        if (newQuery.trim().isEmpty()) {
            _uiState.value = SearchUiState.Initial
        }
    }

    fun searchMovies() {
        val currentQuery = _query.value
        if (currentQuery.isBlank()) return

        viewModelScope.launch {
            validMovies(currentQuery)
        }
    }

    private suspend fun validMovies(query: String) {
        _uiState.value = SearchUiState.Loading
        try {
            val allMovies = searchMovieUseCase(query)
            val validMovies = filterValidMovies(allMovies)

            _uiState.value = if (validMovies.isEmpty()) {
                SearchUiState.Empty
            } else {
                SearchUiState.Success(validMovies)
            }
        } catch (e: Exception) {
            _uiState.value = SearchUiState.Error(e.message ?: UNKNOWN_ERROR)
        }
    }

    private fun filterValidMovies(movies: List<Movie>): List<Movie> {
        return movies.filter { movie ->
            movie.name != null &&
            movie.poster?.url != null &&
            movie.description != null
        }
    }

    companion object {

        private const val UNKNOWN_ERROR = "Unknown error"
    }
}