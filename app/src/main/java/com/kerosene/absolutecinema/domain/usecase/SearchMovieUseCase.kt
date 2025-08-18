package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.SearchRepository
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(
    private val repository: SearchRepository,
) {

    suspend operator fun invoke(query: String) = repository.search(query)
}