package com.kerosene.absolutecinema.di

import android.content.Context
import com.kerosene.absolutecinema.presentation.screens.details.utils.OpenTrailerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    fun provideAppContext(
        @ApplicationContext context: Context
    ): Context {
        return context
    }

    @Provides
    fun provideOpenTrailerHelper(
        @ApplicationContext context: Context
    ): OpenTrailerHelper {
        return OpenTrailerHelper(context)
    }
}