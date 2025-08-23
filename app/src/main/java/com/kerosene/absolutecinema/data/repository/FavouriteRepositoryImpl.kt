package com.kerosene.absolutecinema.data.repository

import com.kerosene.absolutecinema.data.local.db.FavouriteMoviesDao
import com.kerosene.absolutecinema.data.mapper.toDbModel
import com.kerosene.absolutecinema.data.mapper.toMovieList
import com.kerosene.absolutecinema.domain.entity.Movie
import com.kerosene.absolutecinema.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val favouriteMoviesDao: FavouriteMoviesDao,
) : FavouriteRepository {

    override val favouriteMovies: Flow<List<Movie>> =
        favouriteMoviesDao
            .getFavouriteMovies()
            .map { it.toMovieList() }


    override fun observeIsFavourite(movieId: Int): Flow<Boolean> = favouriteMoviesDao
        .observeIsFavourite(movieId)

    override suspend fun addToFavourite(movie: Movie) {
        favouriteMoviesDao.addToFavourite(movie.toDbModel())
    }

    override suspend fun removeFromFavourite(movieId: Int) {
        favouriteMoviesDao.removeFromFavourite(movieId)
    }
}