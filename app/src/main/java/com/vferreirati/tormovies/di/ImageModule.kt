package com.vferreirati.tormovies.di

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(ApplicationComponent::class)
object ImageModule {

    @Provides
    @Singleton
    fun picasso(
        @ApplicationContext context: Context,
        downloader: OkHttp3Downloader
    ): Picasso = Picasso.Builder(context)
        .downloader(downloader)
        .build()
}