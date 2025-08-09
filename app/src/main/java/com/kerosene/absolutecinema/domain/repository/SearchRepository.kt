package com.kerosene.absolutecinema.domain.repository

import com.kerosene.absolutecinema.domain.entity.MovieSearchItem

interface SearchRepository {

    suspend fun search(query: String): List<MovieSearchItem>
}