package com.kerosene.absolutecinema.data.network.dto

import com.google.gson.annotations.SerializedName

data class TrailerDto(
    @SerializedName("url") val trailerUrl: String,
    @SerializedName("name") val name: String,
)
