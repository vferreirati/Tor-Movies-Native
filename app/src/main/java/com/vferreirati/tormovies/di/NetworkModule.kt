package com.vferreirati.tormovies.di

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun okHttpClient(
        networkInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(networkInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun cache(file: File): Cache = Cache(file, 50 * 1024 * 1024)

    @Provides
    @Singleton
    fun file(@ApplicationContext applicationContext: Context): File = File(applicationContext.cacheDir.toURI())

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun okHttpDownloader(cache: File): OkHttp3Downloader = OkHttp3Downloader(cache)
}