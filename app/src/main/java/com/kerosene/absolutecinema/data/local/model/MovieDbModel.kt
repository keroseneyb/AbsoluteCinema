package com.kerosene.absolutecinema.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
data class MovieDbModel(
    @PrimaryKey val id: Int,
    val name: String,
    val year: Int,
    val description: String?,
    val shortDescription: String?,
    val posterUrl: String?,
    val ratingKp: Double,
    val countries: List<String>,
    val genres: List<String>,
)
