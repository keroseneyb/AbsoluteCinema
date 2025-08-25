package com.kerosene.absolutecinema

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.kerosene.absolutecinema.data.di.ApplicationComponent
import com.kerosene.absolutecinema.data.di.DaggerApplicationComponent

class AbsoluteCinemaApp : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    val context = LocalContext.current.applicationContext
    val app = context as? AbsoluteCinemaApp
        ?: error("ApplicationContext is not AbsoluteCinemaApp")
    return app.component
}
