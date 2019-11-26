package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class TorrentWrapperModel(
    @Json(name = "en") val englishTorrents: LanguageWrapperModel?
)