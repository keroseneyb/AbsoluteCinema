package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.repository.MovieRepository
import javax.inject.Inject

class GetReviewsUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(movieId: Int): List<Review> = repository.getReviews(movieId)
}