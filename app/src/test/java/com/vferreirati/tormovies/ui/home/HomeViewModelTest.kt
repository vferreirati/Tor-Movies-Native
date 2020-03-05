package com.vferreirati.tormovies.ui.home

import com.vferreirati.tormovies.BaseUnitTest
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.enums.SortBy
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.data.presentation.MovieImages
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import com.vferreirati.tormovies.data.repository.MoviesRepository
import com.vferreirati.tormovies.utils.Failure
import com.vferreirati.tormovies.utils.Success
import com.vferreirati.tormovies.utils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    // Mock
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
                sizeInBytes = 10,
                fileSize = "",
                magneticUrl = "",
                hasAd = true,
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
                sizeInBytes = 10,
                fileSize = "",
                magneticUrl = "",
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
        )
    )

    // Testing this
    private lateinit var viewModel: HomeViewModel

    @Before
    override fun setup() {
        super.setup()

        val moviesRepository = mockk<MoviesRepository>()
        coEvery { moviesRepository.queryMovies(SortBy.TRENDING, any(), any(), any()) } returns moviesMockMapped
        coEvery { moviesRepository.queryMovies(SortBy.TRENDING, any(), "Deadpool", any()) } returns emptyList()
        coEvery { moviesRepository.queryMovies(SortBy.LAST_ADDED, any(), any(), any()) } returns moviesMockMapped

        viewModel = HomeViewModel(moviesRepository)
    }

    @Test
    fun `Search movies returns mocked movies`() = runBlockingTest {
        val result = viewModel.onSearchMovies("").getOrAwaitValue() as Success

        assertEquals(result.data, moviesMockMapped)
    }

    @Test
    fun `Empty response results in failure`() = runBlockingTest {
        val result = viewModel.onSearchMovies("Deadpool").getOrAwaitValue() as Failure

        assertEquals(result.errorMessageID, R.string.error_query_no_result)
    }
}