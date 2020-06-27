package com.vferreirati.tormovies.data.network.services

import com.vferreirati.tormovies.data.network.model.YtsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YtsMoviesService {

    @GET("list_movies.json")
    suspend fun queryMovies(
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "download_count",
        @Query("order") orderBy: String = "desc",
        @Query("query_term") query: String? = null,
        @Query("genre") genre: String? = null,
        @Query("limit") limit: Int = 50
    ): YtsResponse

}