package com.kerosene.absolutecinema.data.network.dto

import com.google.gson.annotations.SerializedName

data class TrailerResponse(
    @SerializedName("docs") val docs: List<MovieTrailersDto>
)
