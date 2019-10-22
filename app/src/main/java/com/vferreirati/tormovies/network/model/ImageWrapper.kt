package com.vferreirati.tormovies.network.model

import com.squareup.moshi.Json

data class ImageWrapper(
    @field:Json(name = "poster") val posterUrl: String?,
    @field:Json(name = "fanart") val fanArtUrl: String?,
    @field:Json(name = "banner") val bannerUrl: String?
)