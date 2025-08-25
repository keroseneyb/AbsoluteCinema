package com.kerosene.absolutecinema.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kerosene.absolutecinema.data.local.model.MovieDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteMoviesDao {

    @Query("SELECT * FROM favourite_movies")
    fun getFavouriteMovies(): Flow<List<MovieDbModel>>

    @Query("SELECT EXISTS (SELECT * FROM favourite_movies WHERE id=:movieId LIMIT 1)")
    fun observeIsFavourite(movieId: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourite(movieDbModel: MovieDbModel)

    @Query("DELETE FROM favourite_movies WHERE id=:movieId")
    suspend fun removeFromFavourite(movieId: Int)
}