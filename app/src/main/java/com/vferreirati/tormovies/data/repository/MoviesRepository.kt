package com.vferreirati.tormovies.data.repository

import com.vferreirati.tormovies.data.enums.SortBy
import com.vferreirati.tormovies.data.network.services.YtsMoviesService
import com.vferreirati.tormovies.data.presentation.MovieEntry
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val ytsMoviesService: YtsMoviesService
) {

    suspend fun queryMovies(
        sortBy: SortBy,
        page: Int = 1,
        keywords: String? = null,
        genre: String? = null
    ): List<MovieEntry> {
        val response = ytsMoviesService.queryMovies(
            sortBy = sortBy.apiName,
            page = page,
            query = keywords,
            genre = genre
        )

        return response.data.movies.map { MovieEntry(it) }
    }
}