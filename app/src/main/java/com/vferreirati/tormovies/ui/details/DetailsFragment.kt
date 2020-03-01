package com.vferreirati.tormovies.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.se_bastiaan.torrentstream.StreamStatus
import com.github.se_bastiaan.torrentstream.Torrent
import com.github.se_bastiaan.torrentstream.TorrentOptions
import com.github.se_bastiaan.torrentstream.TorrentStream
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import com.vferreirati.tormovies.ui.dialog.SelectQualityDialog
import com.vferreirati.tormovies.utils.getDefaultRequest
import com.vferreirati.tormovies.utils.gone
import com.vferreirati.tormovies.utils.injector
import com.vferreirati.tormovies.utils.visible
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(R.layout.fragment_details), TorrentListener {

    private val args: DetailsFragmentArgs by navArgs()
    private val picasso by lazy { injector.picasso }
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

        if (movie.durationInMinutes != null) {
            txtMovieDuration.text =
                getString(R.string.duration_with_minutes, movie.durationInMinutes)
        } else {
            txtMovieDuration.text = getString(R.string.not_available)
        }

        txtMovieReleaseYear.text = movie.year ?: getString(R.string.not_available)
        val genresBuilder = StringBuilder()
        movie.genres.forEachIndexed { index, s ->
            genresBuilder.append(s)
            if (index != movie.genres.size - 1)
                genresBuilder.append(", ")
        }
        txtMovieGenres.text = genresBuilder.toString()

        btnDownload.setOnClickListener { onDownloadTorrent() }
        btnWatch.setOnClickListener { onWatchTorrent() }
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
        val torrentList = getTorrentList()

        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(torrentList.first().magneticUrl) }
        val resolve = intent.resolveActivity(requireActivity().packageManager)
        if (resolve != null) {
            SelectQualityDialog(torrentList, rewardedAd) { torrentUrl ->
                startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(torrentUrl) })
            }.show(requireActivity().supportFragmentManager, "SelectQualityDialog")

        } else {
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.error)
                .setMessage(R.string.error_opening_torrent_client)
                .setPositiveButton(R.string.ok) { d, _ -> d.dismiss() }
                .show()
        }
    }

    private fun onWatchTorrent() {
        val torrentList = getTorrentList()

        val options = TorrentOptions.Builder()
            .removeFilesAfterStop(true)
            .autoDownload(true)
            .saveLocation(requireActivity().externalCacheDir)
            .build()
        val stream = TorrentStream.init(options).apply { addListener(this@DetailsFragment) }
        stream.startStream(torrentList.first().magneticUrl)
    }

    private fun getTorrentList() : List<MovieTorrent> {
        val movie = args.movieEntry
        val torrentList = mutableListOf<MovieTorrent>()
        movie.hdTorrent?.let { torrentList.add(it) }
        movie.fullHdTorrent?.let { torrentList.add(it) }

        return torrentList
    }

    override fun onStreamReady(torrent: Torrent?) {
        Log.d("TorMoviesLog", "onStreamReady: ${torrent!!.videoFile}")

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(torrent.videoFile.toString()))
        intent.setDataAndType(Uri.parse(torrent.videoFile.toString()), "video/mp4")
        startActivity(intent)
    }

    override fun onStreamPrepared(torrent: Torrent?) {
        Log.d("TorMoviesLog", "OnStreamPrepared")
    }

    override fun onStreamStopped() {
        Log.d("TorMoviesLog", "onStreamStopped")
    }

    override fun onStreamStarted(torrent: Torrent?) {
        Log.d("TorMoviesLog", "onStreamStarted")
    }

    override fun onStreamProgress(torrent: Torrent?, status: StreamStatus?) {
        Log.d("TorMoviesLog", "Progress: ${status?.progress} | Speed: ${status?.downloadSpeed}" )
    }

    override fun onStreamError(torrent: Torrent?, e: Exception?) {
        Log.e("TorMoviesLog", "Got error: $e")
    }
}
