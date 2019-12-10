package com.vferreirati.tormovies.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import com.vferreirati.tormovies.ui.dialog.SelectQualityDialog
import com.vferreirati.tormovies.utils.gone
import com.vferreirati.tormovies.utils.injector
import com.vferreirati.tormovies.utils.visible
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()
    private val picasso by lazy { injector.picasso }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUi()
    }

    private fun initUi() {
        val movie = args.movieEntry

        movie.images.bannerUrl?.let { url ->
            picasso.load(url).placeholder(R.drawable.placeholder_movie_poster).into(imgMoviePoster)
        }

        btnUp.setOnClickListener { findNavController().navigateUp() }

        if (movie.youtubeTrailerUrl != null) {
            btnYoutube.visible()
            btnYoutube.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(movie.youtubeTrailerUrl)
                })
            }
        } else {
            btnYoutube.gone()
        }

        txtMovieName.text = movie.title
        txtMovieSynopsis.text = movie.synopsis
        txtMovieReleaseYear.text = movie.year ?: getString(R.string.not_available)

        if(movie.durationInMinutes != null) {
            txtMovieDuration.text = getString(R.string.duration_with_minutes, movie.durationInMinutes)
        } else {
            txtMovieDuration.text = getString(R.string.not_available)
        }

        txtMovieReleaseYear.text = movie.year ?: getString(R.string.not_available)
        val genresBuilder = StringBuilder()
        movie.genres.forEachIndexed { index, s ->
            genresBuilder.append(s)
            if(index != movie.genres.size - 1)
                genresBuilder.append(", ")
        }
        txtMovieGenres.text = genresBuilder.toString()

        btnDownload.setOnClickListener { onDownloadTorrent() }
    }

    private fun onDownloadTorrent() {
        val movie = args.movieEntry
        val torrentList = mutableListOf<MovieTorrent>()
        movie.hdTorrent?.let { torrentList.add(it) }
        movie.fullHdTorrent?.let { torrentList.add(it) }

        SelectQualityDialog(torrentList).show(activity!!.supportFragmentManager, "SelectQualityDialog")
    }
}
