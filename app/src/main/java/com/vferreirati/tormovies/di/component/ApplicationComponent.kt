package com.vferreirati.tormovies.di.component

import android.content.Context
import com.vferreirati.tormovies.di.modules.ImageModule
import com.vferreirati.tormovies.di.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ ImageModule::class ])
@ApplicationScope
abstract class ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun build(@BindsInstance applicationContext: Context): ApplicationComponent
    }

}