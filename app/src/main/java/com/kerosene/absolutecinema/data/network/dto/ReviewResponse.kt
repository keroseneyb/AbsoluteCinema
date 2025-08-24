package com.kerosene.absolutecinema.data.network.dto

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("docs") val reviews: List<ReviewDto>,
)
