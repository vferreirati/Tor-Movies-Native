package com.vferreirati.tormovies.ui

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
        val movie = args.movieEntry

        movie.images.bannerUrl?.let { url ->
            picasso.load(url).placeholder(R.drawable.placeholder_movie_poster).into(imgMoviePoster)
        }

        btnUp.setOnClickListener { findNavController().navigateUp() }

        if(movie.youtubeTrailerUrl != null) {
            btnYoutube.visible()
            btnYoutube.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(movie.youtubeTrailerUrl) }) }
        } else {
            btnYoutube.gone()
        }
    }
}
