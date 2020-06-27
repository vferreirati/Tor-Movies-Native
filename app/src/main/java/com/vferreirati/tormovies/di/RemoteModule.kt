package com.vferreirati.tormovies.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vferreirati.tormovies.data.network.services.MoviesService
import com.vferreirati.tormovies.data.network.services.YtsMoviesService
import com.vferreirati.tormovies.utils.API_URL
import com.vferreirati.tormovies.utils.YTS_API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(ApplicationComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun retrofit(
        moshiFactory: MoshiConverterFactory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(YTS_API_URL)
        .client(client)
        .addConverterFactory(moshiFactory)
        .build()

    @Provides
    @Singleton
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun moviesService(retrofit: Retrofit): MoviesService = retrofit.create(MoviesService::class.java)

    @Provides
    @Singleton
    fun ytsMoviesService(retrofit: Retrofit): YtsMoviesService = retrofit.create(YtsMoviesService::class.java)
}