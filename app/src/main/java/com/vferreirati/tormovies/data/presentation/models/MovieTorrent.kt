package com.vferreirati.tormovies.data.presentation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieTorrent(
    val quality: String,
    val magneticUrl: String,
    val seedCount: Int,
    val peerCount: Int,
    val fileSize: String,
    val source: String
): Parcelable