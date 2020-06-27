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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list), MovieAdapter.MovieCallback {

    @Inject lateinit var adapter: MovieSearchAdapter

    private val args: ListFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUI()
    }

    private fun initUI() {
        adapter.apply {
            setCallback(this@ListFragment)
            setEntries(args.initialSearchResult.toList())
        }

        val manager = GridLayoutManager(context, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == MovieSearchAdapter.ITEM_VIEW_TYPE)
                    1
                else
                    2
            }
        }

        listMovies.layoutManager = manager
        listMovies.adapter = adapter
    }

    override fun onMovieSelected(movieEntry: MovieEntry) {
        findNavController().navigate(
            ListFragmentDirections.actionListFragmentToDetailsFragment(
                movieEntry
            )
        )
    }
}
