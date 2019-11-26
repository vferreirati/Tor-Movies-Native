package com.vferreirati.tormovies.di.component

import android.content.Context
import com.vferreirati.tormovies.di.modules.ImageModule
import com.vferreirati.tormovies.di.modules.RemoteModule
import com.vferreirati.tormovies.di.scopes.ApplicationScope
import com.vferreirati.tormovies.ui.home.HomeViewModel
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ ImageModule::class, RemoteModule::class ])
@ApplicationScope
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun build(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    val homeViewModel: HomeViewModel
}