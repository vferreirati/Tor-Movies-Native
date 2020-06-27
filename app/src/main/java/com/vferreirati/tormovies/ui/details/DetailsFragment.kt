package com.vferreirati.tormovies.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.squareup.picasso.Picasso
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.ui.dialog.SelectQualityDialog
import com.vferreirati.tormovies.utils.asYoutubeUrl
import com.vferreirati.tormovies.utils.getDefaultRequest
import com.vferreirati.tormovies.utils.gone
import com.vferreirati.tormovies.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    @Inject lateinit var picasso: Picasso
    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var rewardedAd: RewardedAd

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUi()
        initBannerAd()
    }

    override fun onResume() {
        super.onResume()
        loadRewardedAd()
    }

    private fun initUi() {
        val movie = args.movieEntry

        picasso.load(movie.posterImageUrl).placeholder(R.drawable.placeholder_movie_poster).into(imgMoviePoster)

        btnUp.setOnClickListener { findNavController().navigateUp() }

        if (movie.youtubeTrailerCode?.isNotEmpty() == true) {
            btnYoutube.visible()
            btnYoutube.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(movie.youtubeTrailerCode.asYoutubeUrl())
                })
            }
        } else {
            btnYoutube.gone()
        }

        txtMovieName.text = movie.title
        txtMovieSynopsis.text = movie.synopsis
        txtMovieReleaseYear.text = movie.year.toString()

        txtMovieDuration.text = getString(R.string.duration_with_minutes, movie.durationInMinutes)

        val genresBuilder = StringBuilder()
        movie.genres.forEachIndexed { index, s ->
            genresBuilder.append(s)
            if (index != movie.genres.size - 1)
                genresBuilder.append(", ")
        }
        txtMovieGenres.text = genresBuilder.toString()

        btnDownload.setOnClickListener { onDownloadTorrent() }
    }

    private fun initBannerAd() {
        val request = getDefaultRequest()
        detailBannerAd.loadAd(request)
        detailBannerAd.adListener = object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                Log.e("DetailsFragment", errorCode.toString())
            }
        }
    }

    private fun loadRewardedAd() {
        val request = getDefaultRequest()
        rewardedAd = RewardedAd(context, getString(R.string.full_hd_rewarded_ad_id)).apply {
            loadAd(request, object : RewardedAdLoadCallback() {
                override fun onRewardedAdFailedToLoad(errorCode: Int) {
                    Log.e("DetailsFragment", "Got errorCode $errorCode when trying to load AD")
                }

                override fun onRewardedAdLoaded() {
                    Log.v("DetailsFragment", "AD loaded")
                }
            })
        }
    }

    private fun onDownloadTorrent() {
        val movie = args.movieEntry

        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(movie.torrents.first().magneticUrl) }
        val resolve = intent.resolveActivity(requireActivity().packageManager)
        if (resolve != null) {
            SelectQualityDialog(movie.torrents, rewardedAd) { torrentUrl ->
                startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(torrentUrl) })
            }.show(requireActivity().supportFragmentManager, "SelectQualityDialog")

        } else {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.error)
                .setMessage(R.string.error_opening_torrent_client)
                .setPositiveButton(R.string.ok) { d, _ -> d.dismiss() }
                .show()
        }
    }
}
