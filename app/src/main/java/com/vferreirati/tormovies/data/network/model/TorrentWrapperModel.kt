package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class TorrentWrapperModel(
    @field:Json(name = "en") val englishTorrents: LanguageWrapperModel
)