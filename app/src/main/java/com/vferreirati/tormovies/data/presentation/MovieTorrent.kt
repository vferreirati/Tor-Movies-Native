package com.vferreirati.tormovies.data.presentation

data class MovieTorrent(
    val quality: String,
    val magneticUrl: String,
    val seedCount: Int,
    val peerCount: Int,
    val fileSize: String,
    val source: String
)