package com.kerosene.absolutecinema.domain.repository

import androidx.paging.PagingData
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.Trailer
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMoviesPaged(): Flow<PagingData<Movie>>

    fun getPopularMoviesPaged(): Flow<PagingData<Movie>>

    suspend fun getMovieDetails(movieId: Int): Movie

    suspend fun getTrailers(movieId: Int): Result<List<Trailer>>

    suspend fun getReviews(movieId: Int): List<Review>
}