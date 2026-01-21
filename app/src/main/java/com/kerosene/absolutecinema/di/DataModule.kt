package com.kerosene.absolutecinema.di

import android.content.Context
import androidx.room.Room
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
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @Singleton
    @Binds
    fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository

    @Singleton
    @Binds
    fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Singleton
    @Binds
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @Singleton
        @Provides
        fun provideService(): ApiService = ApiFactory.apiService

        @Singleton
        @Provides
        fun provideFavouriteDatabase(
            @ApplicationContext context: Context
        ): FavouriteDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = FavouriteDatabase::class.java,
                name = "favourite_database.db"
            ).build()
        }

        @Singleton
        @Provides
        fun provideFavoriteMoviesDao(database: FavouriteDatabase): FavouriteMoviesDao {
            return database.favouriteMoviesDao()
        }

        @Singleton
        @Provides
        fun provideNotesDao(database: FavouriteDatabase): NotesDao {
            return database.notesDao()
        }
    }
}