package com.vferreirati.tormovies

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.vferreirati.tormovies.di.component.ApplicationComponent
import com.vferreirati.tormovies.di.component.DaggerApplicationComponent

class TorApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().build(this)
        MobileAds.initialize(this@TorApplication)
    }
}