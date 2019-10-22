package com.vferreirati.tormovies.network.model

import com.squareup.moshi.Json

data class TorrentWrapperModel(
    @Json(name = "en") val englishTorrents: LanguageWrapperModel
)