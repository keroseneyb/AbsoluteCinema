package com.kerosene.absolutecinema.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.usecase.SearchMovieUseCase
import com.kerosene.absolutecinema.presentation.screens.search.mapping.toSearchUiModels
import com.kerosene.absolutecinema.presentation.screens.search.model.MovieSearchUiModel
import com.kerosene.absolutecinema.presentation.util.handleApiCall
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
        handleApiCall(
            apiCall = { searchMovieUseCase(query) },
            onLoading = {
                _uiState.value = SearchUiState.Loading
            },
            onSuccess = { movies ->
                val validMovies = filterValidMovies(movies.toSearchUiModels())

                _uiState.value = if (validMovies.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(validMovies)
                }
            },
            onError = { message ->
                _uiState.value = SearchUiState.Error(message)
            }
        )
    }

    private fun filterValidMovies(movies: List<MovieSearchUiModel>): List<MovieSearchUiModel> {
        return movies.filter { movie ->
            with(movie) {
                name.isNotEmpty() &&
                poster.isNotEmpty() &&
                rating != 0.0
            }
        }
    }
}