package com.kerosene.absolutecinema.presentation.screens.search.mapping

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.presentation.screens.search.model.MovieSearchUiModel

fun Movie.toSearchUiModel() : MovieSearchUiModel = MovieSearchUiModel(
    id = id,
    name = name ?: "",
    poster = poster?.url ?: "",
    rating = rating.kp
)

fun List<Movie>.toSearchUiModels() : List<MovieSearchUiModel> = map { it.toSearchUiModel() }