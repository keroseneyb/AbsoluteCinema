package com.kerosene.absolutecinema.presentation.screens.library.favourites.mapping

import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.presentation.screens.library.favourites.model.MovieLibraryUiModel

fun Movie.toMovieLibraryUiModel() : MovieLibraryUiModel = MovieLibraryUiModel(
    id = id,
    poster = poster?.url ?: "",
    title = name ?: ""
)

fun List<Movie>.toMoviesLibraryUiModels() : List<MovieLibraryUiModel> = map { it.toMovieLibraryUiModel() }


