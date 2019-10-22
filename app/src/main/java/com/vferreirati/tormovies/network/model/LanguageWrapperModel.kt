package com.vferreirati.tormovies.network.model

import com.squareup.moshi.Json

data class LanguageWrapperModel(
    @field:Json(name = "1080p") val fullHdTorrent: TorrentModel?,
    @field:Json(name = "720p") val hdTorrent: TorrentModel?
)