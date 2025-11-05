package com.kerosene.absolutecinema.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.entity.Note
import com.kerosene.absolutecinema.domain.usecase.GetMovieDetailsUseCase
import com.kerosene.absolutecinema.domain.usecase.GetReviewsUseCase
import com.kerosene.absolutecinema.domain.usecase.GetTrailersUseCase
import com.kerosene.absolutecinema.domain.usecase.ObserveFavouriteStateUseCase
import com.kerosene.absolutecinema.domain.usecase.ToggleFavouriteStatusUseCase
import com.kerosene.absolutecinema.presentation.screens.details.model.MovieDetailsUiModel
import com.kerosene.absolutecinema.presentation.screens.details.utils.OpenTrailerHelper
import com.kerosene.absolutecinema.presentation.utils.handleApiCall
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
    private val openTrailerHelper: OpenTrailerHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    private val _isFavourite = MutableStateFlow(false)
    val isFavourite: StateFlow<Boolean> = _isFavourite.asStateFlow()

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            preLoadMovie(movieId)
        }
    }

    private suspend fun preLoadMovie(movieId: Int) {
        handleApiCall(
            apiCall = {
                supervisorScope {
                    val movieDeferred = async { getMovieDetailsUseCase(movieId) }
                    val trailersDeferred = async { getTrailersUseCase(movieId) }
                    val reviewsDeferred = async { getReviewsUseCase(movieId) }

                    MovieDetailsUiModel(
                        details = movieDeferred.await(),
                        trailers = trailersDeferred.await(),
                        reviews = reviewsDeferred.await()
                    )
                }
            },
            onSuccess = { uiModel ->
                _uiState.value = MovieDetailsUiState.Success(uiModel)
                observeFavourite(uiModel.details.id)
            },
            onError = { message ->
                _uiState.value = MovieDetailsUiState.Error(message)
            }
        )
    }

    fun observeFavourite(movieId: Int) {
        viewModelScope.launch {
            observeFavouriteStateUseCase(movieId).collect {
                _isFavourite.value = it
            }
        }
    }

    fun toggleFavourite(movieId: Int, title: String) {
        viewModelScope.launch {
            toggleFavouriteStatusUseCase(movieId, title)
        }
    }

    fun openTrailer(url: String) {
        openTrailerHelper.openTrailer(url)
    }
}