package com.vferreirati.tormovies.data.network.model

import com.squareup.moshi.Json

data class YtsResponse(
    @Json(name = "status_message") val statusMessage: String,
    @Json(name = "data") val data: YtsData
)