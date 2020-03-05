package com.vferreirati.tormovies.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import com.vferreirati.tormovies.ui.adapter.TorrentAdapter
import kotlinx.android.synthetic.main.dialog_select_quality.*

class SelectQualityDialog(
    private val torrents: List<MovieTorrent>,
    private val rewardedAd: RewardedAd,
    private val onShowTorrent: (MovieTorrent) -> Unit
) : DialogFragment() {

    private var userGotReward = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_select_quality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listTorrents.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TorrentAdapter(torrents) { torrent -> onTorrentSelected(torrent) }
        }
    }

    private fun onTorrentSelected(torrent: MovieTorrent) {
        if (torrent.hasAd) {
            showAd(torrent)
        } else {
            onShowTorrent(torrent)
        }
        dismiss()
    }

    private fun showAd(torrent: MovieTorrent) {
        if (rewardedAd.isLoaded) {
            rewardedAd.show(activity, object: RewardedAdCallback() {
                override fun onUserEarnedReward(item: RewardItem) {
                    Log.v("SelectQualityDialog", "User got reward")
                    userGotReward = true
                }

                override fun onRewardedAdFailedToShow(errorCode: Int) {
                    Log.e("SelectQualityDialog", "Got error $errorCode when trying to show AD")
                }

                override fun onRewardedAdClosed() {
                    Log.v("SelectQualityDialog", "AD closed")
                    if (userGotReward)
                        onShowTorrent(torrent)
                }

                override fun onRewardedAdOpened() {
                    Log.v("SelectQualityDialog", "AD opened")
                }
            })
        } else {
            onShowTorrent(torrent)
        }
    }
}