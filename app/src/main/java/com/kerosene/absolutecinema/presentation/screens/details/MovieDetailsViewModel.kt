package com.kerosene.absolutecinema.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerosene.absolutecinema.domain.usecase.GetMovieDetailsUseCase
import com.kerosene.absolutecinema.domain.usecase.GetReviewsUseCase
import com.kerosene.absolutecinema.domain.usecase.GetTrailersUseCase
import com.kerosene.absolutecinema.domain.usecase.ObserveFavouriteStateUseCase
import com.kerosene.absolutecinema.domain.usecase.ToggleFavouriteStatusUseCase
import com.kerosene.absolutecinema.presentation.screens.details.model.MovieDetailsUiModel
import com.kerosene.absolutecinema.presentation.screens.details.utils.OpenTrailerHelper
import com.kerosene.absolutecinema.presentation.utils.handleApiCall
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModel @Inject constructor(
    private val observeFavouriteStateUseCase: ObserveFavouriteStateUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getTrailersUseCase: GetTrailersUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val toggleFavouriteStatusUseCase: ToggleFavouriteStatusUseCase,
    private val openTrailerHelper: OpenTrailerHelper,
) : ViewModel() {

    private val currentMovieId = MutableStateFlow<Int?>(null)
    private val movieDetailsUiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)

    val state: StateFlow<MovieDetailsUiState> = combine(
        movieDetailsUiState,
        currentMovieId.filterNotNull().flatMapLatest { observeFavouriteStateUseCase(it) },
    ) { movieDetailsUiState, isFavourite ->
        when (movieDetailsUiState) {
            is MovieDetailsUiState.Loading -> movieDetailsUiState
            is MovieDetailsUiState.Error -> movieDetailsUiState
            is MovieDetailsUiState.Success -> movieDetailsUiState.copy(isFavourite = isFavourite)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MovieDetailsUiState.Loading
    )

    fun loadMovie(movieId: Int) {
        currentMovieId.update { movieId }
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
                movieDetailsUiState.update { MovieDetailsUiState.Success(uiModel) }
            },
            onError = { message ->
                movieDetailsUiState.update { MovieDetailsUiState.Error(message) }
            }
        )
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