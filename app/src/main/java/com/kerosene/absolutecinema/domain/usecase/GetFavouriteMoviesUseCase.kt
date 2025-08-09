package com.kerosene.absolutecinema.domain.usecase

import com.kerosene.absolutecinema.domain.repository.FavouriteRepository
import javax.inject.Inject

class GetFavouriteMoviesUseCase @Inject constructor(
    private val repository: FavouriteRepository,
) {

    operator fun invoke() = repository.favouriteMovies
}