package com.vferreirati.tormovies.data.presentation

import android.os.Parcelable
import com.vferreirati.tormovies.data.network.model.MovieEntry
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieEntry(
    val id: Long,
    val title: String,
    val synopsis: String,
    val year: Int?,
    val durationInMinutes: Long,
    val youtubeTrailerCode: String?,
    val genres: List<String>,
    val posterImageUrl: String,
    val torrents: List<MovieTorrent>
): Parcelable {

    constructor(movieEntry: MovieEntry) : this(
        movieEntry.id,
        movieEntry.title,
        movieEntry.synopsis,
        movieEntry.year,
        movieEntry.runtime,
        movieEntry.ytTrailerCode,
        movieEntry.genres,
        movieEntry.posterImage,
        movieEntry.torrents.map { MovieTorrent(it) }
    )
}