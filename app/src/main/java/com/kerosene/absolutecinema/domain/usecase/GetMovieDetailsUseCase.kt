package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository,
) {

    suspend operator fun invoke(movieId: Int) = repository.getMovieDetails(movieId)
}