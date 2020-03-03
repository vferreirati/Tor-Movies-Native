package com.vferreirati.tormovies.ui.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.enums.SortBy
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.ui.adapter.MovieAdapter
import com.vferreirati.tormovies.ui.adapter.ShimmerAdapter
import com.vferreirati.tormovies.ui.list.ListFragment
import com.vferreirati.tormovies.ui.list.ListFragmentDirections
import com.vferreirati.tormovies.utils.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home), MovieAdapter.MovieCallback {

    private val viewModel by viewModel { injector.homeViewModel }
    private val adapterTrendingMovies by lazy { injector.movieAdapter }
    private val adapterNewMovies by lazy { injector.movieAdapter }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUI()
        adapterTrendingMovies.setCallback(this)
        adapterNewMovies.setCallback(this)
        viewModel.homeState.observe(viewLifecycleOwner, Observer { state -> mapStateToUi(state) })
    }

    private fun initUI() {
        etMovieSearchBar.addDebouncedTextListener(1000L, lifecycle) { query ->
            pbSearch.visible()
            viewModel.onSearchMovies(query).observe(viewLifecycleOwner, Observer { state ->
                mapSearchState(state)
                etMovieSearchBar.setText("")
            })
        }
    }

    private fun mapStateToUi(state: HomeState) = when (state) {
        is MoviesLoaded -> showMovies(state.trendingMovies, state.mostRecentMovies)
        is ErrorLoadingMovies -> onError(state.errorMessageID)
        is LoadingMovies -> showLoadingIndicator()
    }

    private fun showLoadingIndicator() {
        val adapterTrending = ShimmerAdapter(R.layout.list_shimmer_item, 10)
        val adapterRecent = ShimmerAdapter(R.layout.list_shimmer_item, 10)

        listTrendingMovies.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        listTrendingMovies.adapter = adapterTrending

        listNewMovies.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        listNewMovies.adapter = adapterRecent
    }

    private fun onError(messageID: Int) {
        pbSearch.gone()
        Snackbar.make(rootLL, getString(messageID), Snackbar.LENGTH_LONG).show()
    }

    private fun showMovies(trendingMovies: List<MovieEntry>, mostRecentMovies: List<MovieEntry>) {
        listTrendingMovies.adapter = adapterTrendingMovies
        adapterTrendingMovies.setEntries(trendingMovies)

        listNewMovies.adapter = adapterNewMovies
        adapterNewMovies.setEntries(mostRecentMovies)

        btnTrending.setOnClickListener { onSeeTrendingMovies(trendingMovies) }
        btnMostRecent.setOnClickListener { onSeeMostRecentMovies(mostRecentMovies) }
    }

    private fun onSeeMostRecentMovies(entries: List<MovieEntry>) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                entries.toTypedArray(),
                SortBy.LAST_ADDED
            )
        )
    }

    private fun onSeeTrendingMovies(entries: List<MovieEntry>) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                entries.toTypedArray(),
                SortBy.TRENDING
            )
        )
    }

    override fun onMovieSelected(movieEntry: MovieEntry) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                movieEntry
            )
        )
    }

    private fun mapSearchState(event: Event<List<MovieEntry>>) = when (event) {
        is Success -> showSearchResult(event.data)
        is Failure -> onError(event.errorMessageID)
    }

    private fun showSearchResult(movies: List<MovieEntry>) {
        hideKeyboard()
        pbSearch.gone()
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                movies.toTypedArray(),
                SortBy.TRENDING
            )
        )
    }
}
