package com.vferreirati.tormovies.ui.home


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.ui.adapter.ShimmerAdapter
import com.vferreirati.tormovies.utils.activityInjector
import com.vferreirati.tormovies.utils.viewModel
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private val viewModel by viewModel { activityInjector.homeViewModel }
    private val adapterTrendingMovies by lazy { activityInjector.movieAdapter }
    private val adapterNewMovies by lazy { activityInjector.movieAdapter }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.homeState.observe(viewLifecycleOwner, Observer {  state -> mapStateToUi(state) })
    }

    private fun mapStateToUi(state: HomeState) = when(state) {
        is MoviesLoaded -> showMovies(state.trendingMovies, state.mostRecentMovies)
        is ErrorLoadingMovies -> onError(state.errorMessage)
        is LoadingMovies -> showLoadingIndicator()
    }

    private fun showLoadingIndicator() {
        val adapterTrending = ShimmerAdapter(R.layout.list_shimmer_item, 10)
        val adapterRecent = ShimmerAdapter(R.layout.list_shimmer_item, 10)

        listTrendingMovies.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        listTrendingMovies.adapter = adapterTrending

        listNewMovies.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        listNewMovies.adapter = adapterRecent
    }

    private fun onError(errorMessage: String) = Snackbar.make(rootLL, errorMessage, Snackbar.LENGTH_LONG).show()

    private fun showMovies(trendingMovies: List<MovieEntry>, mostRecentMovies: List<MovieEntry>) {
        listTrendingMovies.adapter = adapterTrendingMovies
        adapterTrendingMovies.addEntries(trendingMovies)

        listNewMovies.adapter = adapterNewMovies
        adapterNewMovies.addEntries(mostRecentMovies)
    }
}
