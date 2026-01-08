package com.kerosene.absolutecinema.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.usecase.SearchMovieUseCase
import com.kerosene.absolutecinema.presentation.screens.search.mapping.toSearchUiModels
import com.kerosene.absolutecinema.presentation.screens.search.model.MovieSearchUiModel
import com.kerosene.absolutecinema.presentation.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Initial)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        _query.debounce(500.milliseconds)
            .map { it.trim() }
            .distinctUntilChanged()
            .filter { query ->
                query.length >= 3
            }
            .flatMapLatest { currentQuery ->
                _uiState.update { SearchUiState.Loading }
                searchMovies(currentQuery)
            }
            .onEach { state ->
                _uiState.update { state }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(newQuery: String) {
        _query.update { newQuery }

        if (newQuery.trim().isEmpty()) {
            _uiState.update { SearchUiState.Initial }
        }
    }

    private fun searchMovies(query: String): Flow<SearchUiState> = flow {
        runCatching {
            searchMovieUseCase(query)
        }.fold(
            onSuccess = { movies ->
                val validMovies = filterValidMovies(movies.toSearchUiModels())
                emit(if (validMovies.isEmpty()) {
                        SearchUiState.Empty
                    } else {
                        SearchUiState.Success(validMovies)
                    })
            },
            onFailure = { e ->
                if (e is CancellationException) throw e
                val message = e.message ?: Constants.UNKNOWN_ERROR
                emit(SearchUiState.Error(message))
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