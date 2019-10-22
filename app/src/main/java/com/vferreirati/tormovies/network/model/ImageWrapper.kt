package com.vferreirati.tormovies.network.model

import com.squareup.moshi.Json

data class ImageWrapper(
    @Json(name = "poster") val posterUrl: String?,
    @Json(name = "fanart") val fanArtUrl: String?,
    @Json(name = "banner") val bannerUrl: String?
)