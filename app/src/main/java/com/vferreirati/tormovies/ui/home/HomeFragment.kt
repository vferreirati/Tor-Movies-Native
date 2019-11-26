package com.vferreirati.tormovies.ui.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.utils.activityInjector
import com.vferreirati.tormovies.utils.viewModel


class HomeFragment : Fragment() {

    private val viewModel by viewModel { activityInjector.homeViewModel }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.homeState.observe(viewLifecycleOwner, Observer {  state -> mapStateToUi(state)})
    }

    private fun mapStateToUi(state: HomeState) = when(state) {
        is MoviesLoaded -> showMovies(state.trendingMovies, state.mostRecentMovies)
        is ErrorLoadingMovies -> onError(state.errorMessage)
        is LoadingMovies -> showLoadingIndicator()
    }

    private fun showLoadingIndicator() {
        Log.e("Victor", "Loading")
    }

    private fun onError(errorMessage: String) {
        Log.e("Victor", "error loading movies: $errorMessage!")
    }

    private fun showMovies(trendingMovies: List<MovieEntry>, mostRecentMovies: List<MovieEntry>) {
        Log.e("Victor", "Movies loaded!")
    }
}
