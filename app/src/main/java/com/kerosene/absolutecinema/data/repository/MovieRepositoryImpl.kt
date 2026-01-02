package com.kerosene.absolutecinema.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kerosene.absolutecinema.data.mapper.toMovie
import com.kerosene.absolutecinema.data.mapper.toReview
import com.kerosene.absolutecinema.data.mapper.toTrailer
import com.kerosene.absolutecinema.data.network.api.ApiService
import com.kerosene.absolutecinema.data.network.api.paging.AllMoviesPagingSource
import com.kerosene.absolutecinema.data.network.api.paging.PopularMoviesPagingSource
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.Trailer
import com.kerosene.absolutecinema.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : MovieRepository {

    override fun getMoviesPaged(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { AllMoviesPagingSource(apiService) }
        ).flow.map { pagingData ->
            pagingData.map { movieDto ->
                movieDto.toMovie()
            }
        }
    }

    override fun getPopularMoviesPaged(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { PopularMoviesPagingSource(apiService) }
        ).flow.map { pagingData ->
            pagingData.map { movieDto ->
                movieDto.toMovie()
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Movie {
        return apiService.loadMovieDetails(movieId).toMovie()
    }

    override suspend fun getTrailers(movieId: Int): Result<List<Trailer>> {
        return runCatching {
            apiService.loadTrailers(movieId).docs.mapNotNull { it.toTrailer() }
        }.onFailure { exception ->
            Log.e("MovieRepository", "Error loading trailers: ${exception.message}", exception)
        }
    }

    override suspend fun getReviews(movieId: Int): List<Review> {
        return apiService.loadReviews(movieId).reviews.map { it.toReview() }
    }
}