package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() = repository.getMovies()
}