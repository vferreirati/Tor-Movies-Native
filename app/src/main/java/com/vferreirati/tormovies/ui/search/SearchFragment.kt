package com.vferreirati.tormovies.ui.search


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager

import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieEntry
import com.vferreirati.tormovies.ui.adapter.MovieAdapter
import com.vferreirati.tormovies.ui.adapter.MovieSearchAdapter
import com.vferreirati.tormovies.utils.injector
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(), MovieAdapter.MovieCallback {

    private val args: SearchFragmentArgs by navArgs()

    private val adapter: MovieSearchAdapter by lazy { injector.movieSearchAdapter }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUI()
    }

    private fun initUI() {
        adapter.apply {
            setCallback(this@SearchFragment)
            setEntries(args.initialSearchResult.toList())
        }
        listMovies.layoutManager = GridLayoutManager(context, 2)
        listMovies.adapter = adapter
    }

    override fun onMovieSelected(movieEntry: MovieEntry) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(movieEntry))
    }
}
