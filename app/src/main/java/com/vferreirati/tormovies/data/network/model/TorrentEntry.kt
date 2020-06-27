package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class TorrentEntry(
    @Json(name = "url") val url: String,
    @Json(name = "quality") val quality: String,
    @Json(name = "type") val type: String,
    @Json(name = "seeds") val seeds: Long,
    @Json(name = "peers") val peers: Long,
    @Json(name = "size") val size: String,
    @Json(name = "size_bytes") val sizeBytes: Long,
    @Json(name = "date_uploaded_unix") val dateUploadedUnix: Long
)