package com.kerosene.absolutecinema.domain.repository

import com.kerosene.absolutecinema.domain.entity.Movie
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    val favouriteMovies: Flow<List<Movie>>

    fun observeIsFavourite(movieId: Int): Flow<Boolean>

    suspend fun addToFavourite(movieId: Int)

    suspend fun removeFromFavourite(movieId: Int)
}