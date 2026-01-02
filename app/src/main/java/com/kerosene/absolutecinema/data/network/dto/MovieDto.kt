package com.kerosene.absolutecinema.data.network.dto

import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("rating") val ratingDto: RatingDto,
    @SerializedName("year") val year: Int,
    @SerializedName("countries") val countriesDto: List<CountryDto>,
    @SerializedName("genres") val genresDto: List<GenreDto>?,
    @SerializedName("poster") val posterDto: PosterDto?,
    @SerializedName("description") val description: String,
    @SerializedName("shortDescription") val shortDescription: String
)
