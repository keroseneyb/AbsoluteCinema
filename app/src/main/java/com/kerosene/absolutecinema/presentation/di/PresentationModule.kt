package com.kerosene.absolutecinema.presentation.di

import android.content.Context
import com.kerosene.absolutecinema.presentation.di.qualifers.AppContext
import com.kerosene.absolutecinema.presentation.screens.details.utils.OpenTrailerHelper
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @Provides
    @AppContext
    fun provideAppContext(context: Context): Context {
        return context
    }

    @Provides
    fun provideOpenTrailerHelper(@AppContext context: Context): OpenTrailerHelper {
        return OpenTrailerHelper(context)
    }
}