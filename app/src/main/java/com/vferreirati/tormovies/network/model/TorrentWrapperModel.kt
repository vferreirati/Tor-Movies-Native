package com.vferreirati.tormovies.network.model

import com.squareup.moshi.Json

data class TorrentWrapperModel(
    @field:Json(name = "en") val englishTorrents: LanguageWrapperModel
)