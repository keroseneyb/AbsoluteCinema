package com.kerosene.absolutecinema.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.domain.usecase.GetMovieDetailsUseCase
import com.kerosene.absolutecinema.domain.usecase.GetReviewsUseCase
import com.kerosene.absolutecinema.domain.usecase.GetTrailersUseCase
import com.kerosene.absolutecinema.domain.usecase.ObserveFavouriteStateUseCase
import com.kerosene.absolutecinema.domain.usecase.ToggleFavouriteStatusUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getTrailersUseCase: GetTrailersUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val toggleFavouriteStatusUseCase: ToggleFavouriteStatusUseCase,
    private val observeFavouriteStateUseCase: ObserveFavouriteStateUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Initial)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite: StateFlow<Boolean> = _isFavourite.asStateFlow()

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = MovieDetailsUiState.Loading

            try {
                supervisorScope {
                    val movieDeferred = async { getMovieDetailsUseCase(movieId) }
                    val trailersDeferred = async { getTrailersUseCase(movieId) }
                    val reviewsDeferred = async { getReviewsUseCase(movieId) }

                    val movie = movieDeferred.await()
                    val trailers = trailersDeferred.await()
                    val reviews = reviewsDeferred.await()

                    _uiState.value = MovieDetailsUiState.Success(
                        movie = movie,
                        trailers = trailers,
                        reviews = reviews
                    )

                    observeFavourite(movie.id)
                }
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUiState.Error(
                    message = e.message ?: UNKNOWN_ERROR
                )
            }
        }
    }

    fun observeFavourite(movieId: Int) {
        viewModelScope.launch {
            observeFavouriteStateUseCase(movieId).collect {
                _isFavourite.value = it
            }
        }
    }

    fun toggleFavourite(movie: Movie) {
        viewModelScope.launch {
            toggleFavouriteStatusUseCase(movie)
        }
    }

    companion object {

        private const val UNKNOWN_ERROR = "An unknown error occurred"
    }
}