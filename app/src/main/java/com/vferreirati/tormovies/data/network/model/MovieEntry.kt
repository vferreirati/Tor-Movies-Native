package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class MovieEntry(
    @Json(name = "id") val id: Long,
    @Json(name = "title_english") val title: String,
    @Json(name = "year") val year: Int,
    @Json(name = "runtime") val runtime: Long,
    @Json(name = "genres") val genres: List<String>,
    @Json(name = "synopsis") val synopsis: String,
    @Json(name = "yt_trailer_code") val ytTrailerCode: String?,
    @Json(name = "large_cover_image") val posterImage: String,
    @Json(name = "torrents") val torrents: List<TorrentEntry>
)