package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class TorrentEntryModel(
    @Json(name = "_id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "year") val releaseYear: String?,
    @Json(name = "synopsis") val synopsis: String,
    @Json(name = "runtime") val durationInMinutes: String,
    @Json(name = "trailer") val youtubeTrailerUrl: String?,
    @Json(name = "torrents") val torrents: TorrentWrapperModel,
    @Json(name = "genres") val genres: List<String>,
    @Json(name = "images") val images: ImageWrapper?
)