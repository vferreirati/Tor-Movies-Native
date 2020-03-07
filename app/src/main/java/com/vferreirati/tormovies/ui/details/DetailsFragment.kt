package com.vferreirati.tormovies.ui.details

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.se_bastiaan.torrentstream.StreamStatus
import com.github.se_bastiaan.torrentstream.Torrent
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import com.vferreirati.tormovies.data.services.TorrentDownloadForegroundService
import com.vferreirati.tormovies.ui.dialog.PreparingStreamDialog
import com.vferreirati.tormovies.ui.dialog.SelectQualityDialog
import com.vferreirati.tormovies.utils.*
import kotlinx.android.synthetic.main.fragment_details.*


class DetailsFragment : Fragment(R.layout.fragment_details), TorrentListener {

    private val args: DetailsFragmentArgs by navArgs()
    private val picasso by lazy { injector.picasso }
    private lateinit var rewardedAd: RewardedAd
    private var streamingDialog: PreparingStreamDialog? = null

    private val bindConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {

        }

        override fun onServiceConnected(className: ComponentName?, serviceBinder: IBinder?) {
            val bind = serviceBinder as TorrentDownloadForegroundService.TorrentBinder
            bind.addTorrentListener(this@DetailsFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUi()
        initBannerAd()
    }

    override fun onResume() {
        super.onResume()
        loadRewardedAd()
        stopDownloadService()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_WRITE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED)
                    onWatchTorrent()
                else
                    showPermissionNotGrantedDialog()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
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

        val intent =
            Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(torrentList.first().magneticUrl) }
        val resolve = intent.resolveActivity(requireActivity().packageManager)
        if (resolve != null) {
            SelectQualityDialog(torrentList, rewardedAd) { torrent ->
                startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(torrent.magneticUrl) })
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

        // Check storage permission
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestStoragePermission()
            return
        }

        // Check if user can resolve video intent
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mock.mp4"))
        intent.setDataAndType(Uri.parse("mock.mp4"), "video/mp4")
        val resolve = intent.resolveActivity(requireActivity().packageManager)
        if (resolve == null) {
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.error)
                .setMessage(R.string.movie_player_not_found)
                .setPositiveButton(R.string.ok) { d, _ -> d.dismiss() }
                .show()
        }

        val torrentList = getTorrentList()
        SelectQualityDialog(torrentList, rewardedAd) run@{ torrent ->
            startStream(torrent.magneticUrl)
        }.show(requireActivity().supportFragmentManager, "SelectQualityDialog")
    }

    private fun startStream(torrentUrl: String) {
        val intent = Intent(requireActivity(), TorrentDownloadForegroundService::class.java).apply {
            putExtra(TorrentDownloadForegroundService.TORRENT_URL_KEY, torrentUrl)
        }
        ContextCompat.startForegroundService(requireContext(), intent)
        requireActivity().bindService(intent, bindConnection, Context.BIND_AUTO_CREATE)

        streamingDialog = PreparingStreamDialog() { stopDownloadService() }
        streamingDialog?.show(requireActivity().supportFragmentManager, "PreparingStreamDialog")
    }

    private fun stopDownloadService() {
        requireActivity().stopService(Intent(requireActivity(), TorrentDownloadForegroundService::class.java))
    }

    private fun getTorrentList(): List<MovieTorrent> {
        val movie = args.movieEntry
        val torrentList = mutableListOf<MovieTorrent>()
        movie.hdTorrent?.let { torrentList.add(it) }
        movie.fullHdTorrent?.let { torrentList.add(it) }

        return torrentList
    }

    private fun requestStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_STORAGE
        )
    }

    override fun onStreamReady(torrent: Torrent?) {
        streamingDialog?.dismiss()
        streamingDialog = null

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(torrent!!.videoFile.toString()))
        intent.setDataAndType(Uri.parse(torrent.videoFile.toString()), "video/mp4")
        startActivity(intent)
    }

    private fun showPermissionNotGrantedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.storage_permission_necessary_title))
            .setMessage(getString(R.string.storage_permission_necessary_message))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onStreamPrepared(torrent: Torrent?) {
        Log.d("TorMoviesLog", "OnStreamPrepared")
    }

    override fun onStreamStopped() {
        streamingDialog?.dismiss()
    }

    override fun onStreamStarted(torrent: Torrent?) {
    }

    override fun onStreamProgress(torrent: Torrent?, status: StreamStatus?) {
        streamingDialog?.updateDialog(getString(R.string.downloading_torrent), status?.downloadSpeed?.toMBytes()?.formatMBytes() ?: "", true)
    }

    override fun onStreamError(torrent: Torrent?, e: Exception?) {
        // TODO: Show error message based on exception
    }

    companion object {
        private const val REQUEST_WRITE_STORAGE = 100
    }
}
