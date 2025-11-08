package com.kerosene.absolutecinema.presentation.screens.home.mapping

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.presentation.screens.home.model.MoviePreviewUiModel

fun Movie.toMoviePreviewUiModel() : MoviePreviewUiModel = MoviePreviewUiModel(
    id = id,
    poster = poster?.url ?: "",
    rating = rating.kp
)

fun List<Movie>.toMoviePreviewUiModels() : List<MoviePreviewUiModel> = map { it.toMoviePreviewUiModel() }