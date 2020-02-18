package com.vferreirati.tormovies.ui.list


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager

import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.ui.adapter.MovieAdapter
import com.vferreirati.tormovies.ui.adapter.MovieSearchAdapter
import com.vferreirati.tormovies.utils.injector
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(R.layout.fragment_list), MovieAdapter.MovieCallback {

    private val args: ListFragmentArgs by navArgs()

    private val adapter: MovieSearchAdapter by lazy { injector.movieSearchAdapter }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUI()
    }

    private fun initUI() {
        adapter.apply {
            setCallback(this@ListFragment)
            setEntries(args.initialSearchResult.toList())
        }
        listMovies.layoutManager = GridLayoutManager(context, 2)
        listMovies.adapter = adapter
    }

    override fun onMovieSelected(movieEntry: MovieEntry) {
        findNavController().navigate(ListFragmentDirections.actionListFragmentToDetailsFragment(movieEntry))
    }
}
