package com.kerosene.absolutecinema.data.network.dto

import com.google.gson.annotations.SerializedName

data class TrailersContainerDto(
    @SerializedName("trailers") val trailers: List<TrailerDto>
)
