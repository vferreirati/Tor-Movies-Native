package com.vferreirati.tormovies.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import com.vferreirati.tormovies.ui.adapter.TorrentAdapter
import kotlinx.android.synthetic.main.dialog_select_quality.*

class SelectQualityDialog(
    private val torrents: List<MovieTorrent>
) : DialogFragment() {
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
            adapter = TorrentAdapter(torrents) { url ->
                startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) })
                dismiss()
            }
        }
    }
}