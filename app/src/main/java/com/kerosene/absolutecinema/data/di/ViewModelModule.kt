package com.kerosene.absolutecinema.data.di

import androidx.lifecycle.ViewModel
import com.kerosene.absolutecinema.presentation.screens.details.MovieDetailsViewModel
import com.kerosene.absolutecinema.presentation.screens.home.HomeViewModel
import com.kerosene.absolutecinema.presentation.screens.library.favourites.LibraryViewModel
import com.kerosene.absolutecinema.presentation.screens.library.notes.NoteViewModel
import com.kerosene.absolutecinema.presentation.screens.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LibraryViewModel::class)
    fun bindLibraryViewModel(viewModel: LibraryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    fun bindNoteViewModel(viewModel: NoteViewModel): ViewModel
}