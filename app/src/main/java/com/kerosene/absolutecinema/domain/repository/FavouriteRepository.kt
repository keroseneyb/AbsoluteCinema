package com.kerosene.absolutecinema.domain.repository

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.MoviePreview
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    val favouriteMovies: Flow<List<MoviePreview>>

    fun observeIsFavourite(movieId: Int): Flow<Boolean>

    suspend fun addToFavourite(movie: Movie)

    suspend fun removeFromFavourite(movieId: Int)
}