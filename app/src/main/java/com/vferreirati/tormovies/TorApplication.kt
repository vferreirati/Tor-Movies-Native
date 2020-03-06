package com.vferreirati.tormovies

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
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
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TORRENT_DOWNLOAD_NOTIFICATION_CHANNEL_ID,
                getString(R.string.torrent_download_notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    companion object {
        const val TORRENT_DOWNLOAD_NOTIFICATION_CHANNEL_ID = "TORRENT_DOWNLOAD_NOTIFICATION_CHANNEL"
    }
}