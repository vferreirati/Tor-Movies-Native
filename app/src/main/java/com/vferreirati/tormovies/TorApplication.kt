package com.vferreirati.tormovies

import android.app.Application
import com.vferreirati.tormovies.di.component.ApplicationComponent
import com.vferreirati.tormovies.di.component.DaggerApplicationComponent

class TorApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().build(this)
    }
}