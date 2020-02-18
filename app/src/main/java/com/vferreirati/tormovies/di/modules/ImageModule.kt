package com.vferreirati.tormovies.di.modules

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vferreirati.tormovies.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
object ImageModule {

    @Provides
    @ApplicationScope
    fun picasso(
        context: Context,
        downloader: OkHttp3Downloader
    ): Picasso = Picasso.Builder(context)
        .downloader(downloader)
        .build()
}