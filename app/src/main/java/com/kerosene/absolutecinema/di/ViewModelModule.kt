package com.kerosene.absolutecinema.di

import androidx.lifecycle.ViewModel
import com.kerosene.absolutecinema.presentation.screens.details.MovieDetailsViewModel
import com.kerosene.absolutecinema.presentation.screens.home.HomeViewModel
import com.kerosene.absolutecinema.presentation.screens.library.favourites.LibraryViewModel
import com.kerosene.absolutecinema.presentation.screens.library.notes.NoteViewModel
import com.kerosene.absolutecinema.presentation.screens.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel

    @Binds
    fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    fun bindLibraryViewModel(viewModel: LibraryViewModel): ViewModel

    @Binds
    fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    fun bindNoteViewModel(viewModel: NoteViewModel): ViewModel
}