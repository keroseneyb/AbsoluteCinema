package com.kerosene.absolutecinema.domain.repository

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.Trailer

interface MovieRepository {

    suspend fun getMovies(): List<Movie>

    suspend fun getPopularMovies(): List<Movie>

    suspend fun getTrailers(movieId: Int): List<Trailer>

    suspend fun getReviews(movieId: Int): List<Review>
}