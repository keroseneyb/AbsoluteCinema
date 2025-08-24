package com.kerosene.absolutecinema.data.network.dto

import com.google.gson.annotations.SerializedName

data class PosterDto(
    @SerializedName("url") val url: String?,
)
