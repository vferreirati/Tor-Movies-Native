package com.vferreirati.tormovies.network.model

import com.squareup.moshi.Json

data class TorrentEntryModel(
    @field:Json(name = "_id") val id: String,
    @field:Json(name = "imdb_id") val imdbId: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "year") val releaseYear: String,
    @field:Json(name = "synopsis") val synopsis: String,
    @field:Json(name = "runtime") val durationInMinutes: String,
    @field:Json(name = "trailer") val youtubeTrailerUrl: String?,
    @field:Json(name = "torrents") val torrents: TorrentWrapperModel,
    @field:Json(name = "genres") val genres: List<String>,
    @field:Json(name = "images") val images: ImageWrapper
)