package com.vferreirati.tormovies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.vferreirati.tormovies.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = RequestConfiguration.Builder()
            .setTestDeviceIds(listOf("11A8B1813CA154DBAFFF51CCF5584D50"))
            .build()
        MobileAds.setRequestConfiguration(config)
    }
}
