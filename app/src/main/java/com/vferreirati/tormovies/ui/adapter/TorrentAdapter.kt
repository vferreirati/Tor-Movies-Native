package com.vferreirati.tormovies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.data.presentation.MovieTorrent
import kotlinx.android.synthetic.main.item_list_torrent.view.*

class TorrentAdapter(
    private val torrents: List<MovieTorrent>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<TorrentAdapter.TorrentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_torrent, parent, false)
        return TorrentHolder(view)
    }

    override fun getItemCount(): Int = torrents.size

    override fun onBindViewHolder(holder: TorrentHolder, position: Int) {
        val torrent = torrents[position]
        val context = holder.itemView.context
        val hasAdResId = if (torrent.hasAd) R.string.yes else R.string.no

        holder.txtFileSize.text = torrent.fileSize
        holder.txtTorrentQuality.text = torrent.quality
        holder.txtTorrentSource.text = torrent.source
        holder.txtPeerCount.text = torrent.peerCount.toString()
        holder.txtSeedCount.text = torrent.seedCount.toString()
        holder.txtAdToDownload.text = context.getString(hasAdResId)

        holder.itemView.setOnClickListener { onClick(torrent.magneticUrl) }
    }

    inner class TorrentHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtTorrentQuality: AppCompatTextView = itemView.txtTorrentQuality
        val txtSeedCount: AppCompatTextView = itemView.txtSeedCount
        val txtPeerCount: AppCompatTextView = itemView.txtPeerCount
        val txtFileSize: AppCompatTextView = itemView.txtFileSize
        val txtTorrentSource: AppCompatTextView = itemView.txtTorrentSource
        val txtAdToDownload: AppCompatTextView = itemView.txtHasAd
    }
}