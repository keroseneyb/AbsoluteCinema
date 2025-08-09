package com.kerosene.absolutecinema.domain.entity

data class Review(
    val author: String,
    val title: String,
    val review: String,
    val type: ReviewType
)

enum class ReviewType(val value: String) {

    POSITIVE("Позитивный"),
    NEGATIVE("Негативный"),
    NEUTRAL("Нейтральный");

    companion object {
        fun fromValue(raw: String): ReviewType = entries.find {
            it.value.equals(raw, ignoreCase = true)
        } ?: NEUTRAL
    }
}
