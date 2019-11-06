package com.vferreirati.tormovies.di.modules

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.OkHttp3Downloader
import com.vferreirati.tormovies.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    @Provides
    @ApplicationScope
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    @ApplicationScope
    fun moshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @ApplicationScope
    fun okHttpClient(
        networkInterceptor: HttpLoggingInterceptor,
        cache: Cache
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(networkInterceptor)
        .cache(cache)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    @Provides
    @ApplicationScope
    fun cache(file: File): Cache = Cache(file, 50 * 1024 * 1024)

    @Provides
    @ApplicationScope
    fun file(applicationContext: Context): File = File(applicationContext.cacheDir.toURI())

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun okHttpDownloader(cache: File): OkHttp3Downloader = OkHttp3Downloader(cache)
}