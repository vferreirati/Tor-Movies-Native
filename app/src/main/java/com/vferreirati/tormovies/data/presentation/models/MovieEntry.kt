package com.vferreirati.tormovies.data.presentation.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieEntry(
    val id: String?,
    val title: String,
    val synopsis: String,
    val year: String?,
    val durationInMinutes: String?,
    val youtubeTrailerUrl: String?,
    val genres: List<String>,
    val images: MovieImages,
    val fullHdTorrent: MovieTorrent?,
    val hdTorrent: MovieTorrent?
): Parcelable