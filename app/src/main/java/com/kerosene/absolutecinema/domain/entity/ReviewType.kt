package com.kerosene.absolutecinema.domain.entity

enum class ReviewType(val value: String) {

    POSITIVE(ReviewValues.POSITIVE),
    NEGATIVE(ReviewValues.NEGATIVE),
    NEUTRAL(ReviewValues.NEUTRAL);

    companion object {

        fun fromValue(raw: String): ReviewType = entries.find {
            it.value.equals(raw, ignoreCase = true)
        } ?: NEUTRAL
    }
}

object ReviewValues {

    const val POSITIVE = "Позитивный"
    const val NEGATIVE = "Негативный"
    const val NEUTRAL = "Нейтральный"
}