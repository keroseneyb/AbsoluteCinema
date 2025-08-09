package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.FavouriteRepository
import javax.inject.Inject

class ObserveFavouriteStateUseCase @Inject constructor(
    private val repository: FavouriteRepository,
) {

    operator fun invoke(movieId: Int) = repository.observeIsFavourite(movieId)
}