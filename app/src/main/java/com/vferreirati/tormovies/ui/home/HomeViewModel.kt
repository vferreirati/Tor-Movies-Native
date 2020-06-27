package com.vferreirati.tormovies.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.enums.SortBy
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.data.repository.MoviesRepository
import com.vferreirati.tormovies.utils.Event
import com.vferreirati.tormovies.utils.Failure
import com.vferreirati.tormovies.utils.SingleLiveEvent
import com.vferreirati.tormovies.utils.Success
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _homeState = MutableLiveData<HomeState>()
    val homeState get() = _homeState as LiveData<HomeState>

    init { loadMovies() }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                _homeState.postValue(LoadingMovies)

                val trendingMovies = moviesRepository.queryMovies(sortBy = SortBy.TRENDING)
                val mostRecent = moviesRepository.queryMovies(sortBy = SortBy.LAST_ADDED)

                _homeState.postValue(MoviesLoaded(trendingMovies = trendingMovies, mostRecentMovies = mostRecent))
            } catch (t: Throwable) {
                t.printStackTrace()
                _homeState.postValue(ErrorLoadingMovies(R.string.error_check_internet))
            }
        }
    }

    fun onSearchMovies(query: String): SingleLiveEvent<Event<List<MovieEntry>>>  {
        val result = SingleLiveEvent<Event<List<MovieEntry>>>()

        viewModelScope.launch {
            try {
                val moviesResult = moviesRepository.queryMovies(keywords = query, sortBy = SortBy.TRENDING)
                if (moviesResult.isEmpty()) {
                    result.value = Failure(R.string.error_query_no_result)

                } else {
                    result.value = Success(moviesResult)
                }

            } catch (t: Throwable) {
                t.printStackTrace()
                result.value = Failure(R.string.error_check_internet)
            }
        }

        return result
    }
}

sealed class HomeState
class MoviesLoaded(
    val trendingMovies: List<MovieEntry>,
    val mostRecentMovies: List<MovieEntry>
) : HomeState()
class ErrorLoadingMovies(val errorMessageID: Int) : HomeState()
object LoadingMovies : HomeState()