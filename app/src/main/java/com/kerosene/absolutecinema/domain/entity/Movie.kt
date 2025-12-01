package com.kerosene.absolutecinema.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val name: String?,
    val rating: Rating,
    val year: Int,
    val country: List<Country>,
    val genre: List<Genre>,
    val description: String?,
    val shortDescription: String?,
    val poster: Poster?,
)
