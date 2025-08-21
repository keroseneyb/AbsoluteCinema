package com.kerosene.absolutecinema.domain.entity

data class Note(
    val id: Int,
    val movieId: Int,
    val title: String,
    val content: String,
)
