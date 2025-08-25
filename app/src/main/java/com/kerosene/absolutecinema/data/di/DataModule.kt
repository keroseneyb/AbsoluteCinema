package com.kerosene.absolutecinema.data.di

import android.content.Context
import com.kerosene.absolutecinema.data.local.db.FavouriteDatabase
import com.kerosene.absolutecinema.data.local.db.FavouriteMoviesDao
import com.kerosene.absolutecinema.data.local.db.NotesDao
import com.kerosene.absolutecinema.data.network.api.ApiFactory
import com.kerosene.absolutecinema.data.network.api.ApiService
import com.kerosene.absolutecinema.data.repository.FavouriteRepositoryImpl
import com.kerosene.absolutecinema.data.repository.MovieRepositoryImpl
import com.kerosene.absolutecinema.data.repository.NoteRepositoryImpl
import com.kerosene.absolutecinema.data.repository.SearchRepositoryImpl
import com.kerosene.absolutecinema.domain.repository.FavouriteRepository
import com.kerosene.absolutecinema.domain.repository.MovieRepository
import com.kerosene.absolutecinema.domain.repository.NoteRepository
import com.kerosene.absolutecinema.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @ApplicationScope
    @Binds
    fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository

    @ApplicationScope
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @ApplicationScope
    @Binds
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideService(): ApiService = ApiFactory.apiService

        @ApplicationScope
        @Provides
        fun provideFavouriteDatabase(context: Context): FavouriteDatabase {
            return FavouriteDatabase.getInstance(context)
        }

        @ApplicationScope
        @Provides
        fun provideFavoriteMoviesDao(database: FavouriteDatabase): FavouriteMoviesDao {
            return database.favouriteMoviesDao()
        }

        @ApplicationScope
        @Provides
        fun provideNotesDao(database: FavouriteDatabase): NotesDao {
            return database.notesDao()
        }
    }
}