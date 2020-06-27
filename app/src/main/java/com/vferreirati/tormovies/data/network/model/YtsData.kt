package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class YtsData(
    @Json(name = "movie_count") val movieCount: Int,
    @Json(name = "limit") val limit: Int,
    @Json(name = "page_number") val pageNumber: Int,
    @Json(name = "movies") val movies: List<MovieEntry>
)