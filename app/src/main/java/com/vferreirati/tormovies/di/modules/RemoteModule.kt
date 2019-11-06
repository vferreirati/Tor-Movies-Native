package com.vferreirati.tormovies.di.modules

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.vferreirati.tormovies.di.scopes.ApplicationScope
import com.vferreirati.tormovies.network.services.MoviesService
import com.vferreirati.tormovies.utils.API_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module(includes = [ NetworkModule::class ])
object RemoteModule {

    @Provides
    @ApplicationScope
    fun retrofit(
        moshiFactory: MoshiConverterFactory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(API_URL)
        .client(client)
        .addConverterFactory(moshiFactory)
        .build()

    @Provides
    fun moviesService(retrofit: Retrofit): MoviesService = retrofit.create(MoviesService::class.java)

    @Provides
    @ApplicationScope
    fun moshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    @ApplicationScope
    fun moshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}