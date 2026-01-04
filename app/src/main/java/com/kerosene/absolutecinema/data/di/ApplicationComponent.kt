package com.kerosene.absolutecinema.data.di

import android.content.Context
import com.kerosene.absolutecinema.presentation.di.ViewModelFactory
import com.kerosene.absolutecinema.presentation.di.PresentationModule
import dagger.BindsInstance
import dagger.Component
// asdadasd
@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        PresentationModule::class
    ]
)
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): ApplicationComponent
    }
}