package com.kerosene.absolutecinema.data.network.dto

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("docs") val movies: List<MovieDto>,
)