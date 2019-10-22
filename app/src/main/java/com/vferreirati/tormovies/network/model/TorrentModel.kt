package com.vferreirati.tormovies.network.model

import com.squareup.moshi.Json

data class TorrentModel(
    @Json(name = "url") val magneticUrl: String,
    @Json(name = "seed") val seeders: Int,
    @Json(name = "peer") val peers: Int,
    @Json(name = "filesize") val fileSize: String,
    @Json(name = "provider") val source: String
)