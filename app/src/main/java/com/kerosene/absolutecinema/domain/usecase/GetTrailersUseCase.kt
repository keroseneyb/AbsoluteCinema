package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.entity.Trailer
import com.kerosene.absolutecinema.domain.repository.MovieRepository
import javax.inject.Inject

class GetTrailersUseCase @Inject constructor(
    private val repository: MovieRepository,
) {

    suspend operator fun invoke(movieId: Int): List<Trailer> = repository.getTrailers(movieId)
}