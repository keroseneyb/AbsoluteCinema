package com.kerosene.absolutecinema.domain.entity

data class MovieSearchItem(
    val id: Int,
    val name: String,
    val poster: Poster,
    val rating: Rating
)
