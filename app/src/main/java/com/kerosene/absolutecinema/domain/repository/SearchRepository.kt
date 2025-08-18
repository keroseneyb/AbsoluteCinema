package com.kerosene.absolutecinema.domain.repository

import com.kerosene.absolutecinema.domain.entity.Movie

interface SearchRepository {

    suspend fun search(query: String): List<Movie>
}