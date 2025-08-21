package com.kerosene.absolutecinema.domain.entity

data class Review(
    val author: String,
    val title: String,
    val review: String,
    val type: ReviewType,
)

