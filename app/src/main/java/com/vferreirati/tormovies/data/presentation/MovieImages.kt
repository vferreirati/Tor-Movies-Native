package com.vferreirati.tormovies.data.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieImages(
    val posterUrl: String?,
    val fanArtUrl: String?,
    val bannerUrl: String?
): Parcelable