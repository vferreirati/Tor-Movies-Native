package com.vferreirati.tormovies.data.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieTorrent(
    val quality: String,
    val magneticUrl: String,
    val seedCount: Int,
    val peerCount: Int,
    val fileSize: String,
    val source: String,
    val hasAd: Boolean
): Parcelable