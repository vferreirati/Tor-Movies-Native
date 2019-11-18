package com.vferreirati.tormovies.data.network.services

import com.vferreirati.tormovies.data.network.model.TorrentEntryModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("movies/{page}")
    suspend fun queryMovies(
        @Path("page") page: Int = 1,
        @Query("sort") sortBy: String = "trending",
        @Query("order") orderBy: Int = -1,
        @Query("keywords") query: String? = null,
        @Query("genre") genre: String? = null
    ): List<TorrentEntryModel>
}