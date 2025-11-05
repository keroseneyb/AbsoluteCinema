package com.kerosene.absolutecinema.presentation.screens.details.model

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.entity.Review
import com.kerosene.absolutecinema.domain.entity.Trailer

data class MovieDetailsUiModel(
    val details: Movie,
    val trailers: List<Trailer>,
    val reviews: List<Review>,
)
