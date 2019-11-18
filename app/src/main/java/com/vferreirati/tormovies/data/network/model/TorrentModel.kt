package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class TorrentModel(
    @field:Json(name = "url") val magneticUrl: String,
    @field:Json(name = "seed") val seeders: Int,
    @field:Json(name = "peer") val peers: Int,
    @field:Json(name = "filesize") val fileSize: String,
    @field:Json(name = "provider") val source: String
)