package com.kerosene.absolutecinema.data.repository

import com.kerosene.absolutecinema.data.mapper.toMovie
import com.kerosene.absolutecinema.data.network.api.ApiService
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : SearchRepository {

    override suspend fun search(query: String): List<Movie> {
        return apiService.searchMovie(query).movies.map { it.toMovie() }
    }
}