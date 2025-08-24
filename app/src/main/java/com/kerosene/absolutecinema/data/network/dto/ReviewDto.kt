package com.kerosene.absolutecinema.data.network.dto

import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("author") val author: String,
    @SerializedName("title") val title: String,
    @SerializedName("review") val review: String,
    @SerializedName("type") val type: String
)
