package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class LanguageWrapperModel(
    @Json(name = "1080p") val fullHdTorrent: TorrentModel?,
    @Json(name = "720p") val hdTorrent: TorrentModel?
)