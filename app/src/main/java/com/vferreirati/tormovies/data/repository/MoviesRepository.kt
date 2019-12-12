package com.vferreirati.tormovies.data.repository

import com.vferreirati.tormovies.data.enums.SortBy
import com.vferreirati.tormovies.data.network.model.TorrentEntryModel
import com.vferreirati.tormovies.data.network.model.TorrentModel
import com.vferreirati.tormovies.data.network.services.MoviesService
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.data.presentation.MovieImages
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val moviesService: MoviesService
) {

    suspend fun queryMovies(
        sortBy: SortBy,
        page: Int = 1,
        keywords: String? = null,
        genre: String? = null
    ): List<MovieEntry> {
        val response = moviesService.queryMovies(
            sortBy = sortBy.apiName,
            page = page,
            query = keywords,
            genre = genre
        )
        return response.map { entry -> mapMovieToDomain(entry) }.toList()
    }

    private fun mapMovieToDomain(entry: TorrentEntryModel): MovieEntry {
        return MovieEntry(
            id = entry.id,
            title = entry.title,
            durationInMinutes = entry.durationInMinutes,
            genres = entry.genres,
            synopsis = entry.synopsis,
            year = entry.releaseYear,
            youtubeTrailerUrl = entry.youtubeTrailerUrl,
            images = MovieImages(
                posterUrl = entry.images.posterUrl?.replace("http://", "https://"),
                bannerUrl = entry.images.bannerUrl?.replace("http://", "https://"),
                fanArtUrl = entry.images.fanArtUrl?.replace("http://", "https://")
            ),
            fullHdTorrent = entry.torrents.englishTorrents?.fullHdTorrent?.let { tor ->
                mapTorrentToDomain(
                    tor,
                    "1080p"
                )
            },
            hdTorrent = entry.torrents.englishTorrents?.hdTorrent?.let { tor ->
                mapTorrentToDomain(
                    tor,
                    "720p"
                )
            }
        )
    }

    private fun mapTorrentToDomain(tor: TorrentModel, quality: String): MovieTorrent {
        return MovieTorrent(
            quality = quality,
            fileSize = tor.fileSize,
            magneticUrl = tor.magneticUrl,
            peerCount = tor.peers,
            seedCount = tor.seeders,
            source = tor.source
        )
    }
}