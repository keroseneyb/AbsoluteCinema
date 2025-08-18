package com.kerosene.absolutecinema.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
): Parcelable
