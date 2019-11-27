package com.vferreirati.tormovies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.data.repository.MoviesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _homeState = MutableLiveData<HomeState>()
    val homeState get() = _homeState as LiveData<HomeState>

    init { loadMovies() }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                _homeState.postValue(LoadingMovies)
                val trendingMovies = moviesRepository.queryMovies(sortBy = "trending")
                val mostRecent = moviesRepository.queryMovies(sortBy = "last added")

                _homeState.postValue(MoviesLoaded(trendingMovies = trendingMovies, mostRecentMovies = mostRecent))
            } catch (t: Throwable) {
                t.printStackTrace()
                _homeState.postValue(ErrorLoadingMovies("Check your internet connection"))
            }
        }
    }
}

sealed class HomeState
class MoviesLoaded(
    val trendingMovies: List<MovieEntry>,
    val mostRecentMovies: List<MovieEntry>
) : HomeState()
class ErrorLoadingMovies(val errorMessage: String) : HomeState()
object LoadingMovies : HomeState()