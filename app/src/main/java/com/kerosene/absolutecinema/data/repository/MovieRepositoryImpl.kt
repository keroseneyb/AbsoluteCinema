package com.kerosene.absolutecinema.data.repository

import com.kerosene.absolutecinema.data.mapper.toMovie
import com.kerosene.absolutecinema.data.mapper.toReview
import com.kerosene.absolutecinema.data.mapper.toTrailer
import com.kerosene.absolutecinema.data.network.api.ApiService
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.Trailer
import com.kerosene.absolutecinema.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : MovieRepository {

    override suspend fun getMovieDetails(movieId: Int): Movie {
        return apiService.loadMovieDetails(movieId).toMovie()
    }

    override suspend fun getMovies(): List<Movie> {
        return apiService.loadAllMovies().movies.map { it.toMovie() }
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return apiService.loadPopularMovies().movies.map { it.toMovie() }
    }

    override suspend fun getTrailers(movieId: Int): List<Trailer> {
        return apiService.loadTrailers(movieId).docs.map { it.toTrailer() }
    }

    override suspend fun getReviews(movieId: Int): List<Review> {
        return apiService.loadReviews(movieId).reviews.map { it.toReview() }
    }
}