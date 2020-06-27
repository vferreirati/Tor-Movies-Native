package com.vferreirati.tormovies.data.presentation

import android.os.Parcelable
import com.vferreirati.tormovies.data.network.model.TorrentEntry
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieTorrent(
    val quality: String,
    val magneticUrl: String,
    val seedCount: Long,
    val peerCount: Long,
    val fileSize: String,
    val hasAd: Boolean
): Parcelable {
    constructor(torrentEntry: TorrentEntry) : this(
        torrentEntry.quality,
        torrentEntry.url,
        torrentEntry.seeds,
        torrentEntry.peers,
        torrentEntry.size,
        true
    )
}