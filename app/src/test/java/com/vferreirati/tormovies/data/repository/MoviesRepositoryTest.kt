package com.vferreirati.tormovies.data.repository

import com.vferreirati.tormovies.BaseUnitTest
import com.vferreirati.tormovies.data.enums.SortBy
import com.vferreirati.tormovies.data.network.model.LanguageWrapperModel
import com.vferreirati.tormovies.data.network.model.TorrentEntryModel
import com.vferreirati.tormovies.data.network.model.TorrentModel
import com.vferreirati.tormovies.data.network.model.TorrentWrapperModel
import com.vferreirati.tormovies.data.network.services.MoviesService
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.data.presentation.MovieImages
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesRepositoryTest : BaseUnitTest() {

    // Mocks
    private val movieService = mockk<MoviesService>()
    private val moviesMock: List<TorrentEntryModel> = listOf(
        TorrentEntryModel(
            id = "id",
            title = "Movie 01",
            releaseYear = null,
            synopsis = "Movie 01 syn",
            durationInMinutes = "100",
            youtubeTrailerUrl = null,
            genres = listOf("genre 01", "genre 02"),
            images = null,
            torrents = TorrentWrapperModel(
                englishTorrents = LanguageWrapperModel(
                    hdTorrent = TorrentModel(
                        magneticUrl = "",
                        fileSize = "",
                        sizeInBytes = 10,
                        peers = 0,
                        seeders = 0,
                        source = ""
                    ),
                    fullHdTorrent = TorrentModel(
                        magneticUrl = "",
                        fileSize = "",
                        sizeInBytes = 10,
                        peers = 0,
                        seeders = 0,
                        source = ""
                    )
                )
            )
        ),
        TorrentEntryModel(
            id = "id",
            title = "Movie 02",
            releaseYear = null,
            synopsis = "Movie 02 syn",
            durationInMinutes = "100",
            youtubeTrailerUrl = null,
            genres = listOf("genre 01", "genre 02"),
            images = null,
            torrents = TorrentWrapperModel(
                englishTorrents = LanguageWrapperModel(
                    hdTorrent = TorrentModel(
                        magneticUrl = "",
                        fileSize = "",
                        peers = 0,
                        seeders = 0,
                        sizeInBytes = 10,
                        source = ""
                    ),
                    fullHdTorrent = TorrentModel(
                        magneticUrl = "",
                        fileSize = "",
                        peers = 0,
                        sizeInBytes = 10,
                        seeders = 0,
                        source = ""
                    )
                )
            )
        ),
        TorrentEntryModel(
            id = "id",
            title = "Movie 03",
            releaseYear = null,
            synopsis = "Movie 03 syn",
            durationInMinutes = "100",
            youtubeTrailerUrl = null,
            genres = listOf("genre 01", "genre 02"),
            images = null,
            torrents = TorrentWrapperModel(
                englishTorrents = LanguageWrapperModel(
                    hdTorrent = TorrentModel(
                        magneticUrl = "",
                        fileSize = "",
                        peers = 0,
                        sizeInBytes = 10,
                        seeders = 0,
                        source = ""
                    ),
                    fullHdTorrent = TorrentModel(
                        magneticUrl = "",
                        fileSize = "",
                        peers = 0,
                        seeders = 0,
                        sizeInBytes = 10,
                        source = ""
                    )
                )
            )
        )
    )
    private val moviesMockMapped: List<MovieEntry> = listOf(
        MovieEntry(
            id = "id",
            title = "Movie 01",
            genres = listOf("genre 01", "genre 02"),
            youtubeTrailerUrl = null,
            durationInMinutes = "100",
            synopsis = "Movie 01 syn",
            year = null,
            images = MovieImages(null, null, null),
            fullHdTorrent = MovieTorrent(
                quality = "1080p",
                source = "",
                fileSize = "",
                magneticUrl = "",
                hasAd = true,
                peerCount = 0,
                sizeInBytes = 10,
                seedCount = 0
            ),
            hdTorrent = MovieTorrent(
                quality = "720p",
                source = "",
                fileSize = "",
                magneticUrl = "",
                hasAd = false,
                peerCount = 0,
                sizeInBytes = 10,
                seedCount = 0
            )
        ),
        MovieEntry(
            id = "id",
            title = "Movie 02",
            genres = listOf("genre 01", "genre 02"),
            youtubeTrailerUrl = null,
            durationInMinutes = "100",
            synopsis = "Movie 02 syn",
            year = null,
            images = MovieImages(null, null, null),
            fullHdTorrent = MovieTorrent(
                quality = "1080p",
                source = "",
                fileSize = "",
                magneticUrl = "",
                hasAd = true,
                sizeInBytes = 10,
                peerCount = 0,
                seedCount = 0
            ),
            hdTorrent = MovieTorrent(
                quality = "720p",
                source = "",
                fileSize = "",
                magneticUrl = "",
                sizeInBytes = 10,
                hasAd = false,
                peerCount = 0,
                seedCount = 0
            )
        ),
        MovieEntry(
            id = "id",
            title = "Movie 03",
            genres = listOf("genre 01", "genre 02"),
            youtubeTrailerUrl = null,
            durationInMinutes = "100",
            synopsis = "Movie 03 syn",
            year = null,
            images = MovieImages(null, null, null),
            fullHdTorrent = MovieTorrent(
                quality = "1080p",
                source = "",
                fileSize = "",
                magneticUrl = "",
                sizeInBytes = 10,
                hasAd = true,
                peerCount = 0,
                seedCount = 0
            ),
            hdTorrent = MovieTorrent(
                quality = "720p",
                source = "",
                fileSize = "",
                magneticUrl = "",
                hasAd = false,
                sizeInBytes = 10,
                peerCount = 0,
                seedCount = 0
            )
        )
    )

    // Testing this
    private lateinit var moviesRepository: MoviesRepository

    @Before
    override fun setup() {
        super.setup()

        coEvery { movieService.queryMovies() } returns moviesMock
        moviesRepository = MoviesRepository(movieService)
    }

    @Test
    fun `Query movies get all movies`() = runBlockingTest {
        val movies =  moviesRepository.queryMovies(SortBy.TRENDING)
        assertEquals(movies, moviesMockMapped)
    }
}