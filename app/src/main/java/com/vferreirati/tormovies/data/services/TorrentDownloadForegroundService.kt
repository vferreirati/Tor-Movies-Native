package com.vferreirati.tormovies.data.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.se_bastiaan.torrentstream.StreamStatus
import com.github.se_bastiaan.torrentstream.Torrent
import com.github.se_bastiaan.torrentstream.TorrentOptions
import com.github.se_bastiaan.torrentstream.TorrentStream
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener
import com.vferreirati.tormovies.R
import com.vferreirati.tormovies.TorApplication
import com.vferreirati.tormovies.utils.formatMBytes
import com.vferreirati.tormovies.utils.toMBytes

class TorrentDownloadForegroundService : Service(), TorrentListener {

    companion object {
        private const val DOWNLOAD_NOTIFICATION_ID = 100
        const val TORRENT_URL_KEY = "TORRENT_URL_KEY"
    }

    private var currentStream: TorrentStream? = null

    override fun onDestroy() {
        super.onDestroy()
        currentStream?.stopStream()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val torrentMagneticUrl = intent?.getStringExtra(TORRENT_URL_KEY) ?: throw IllegalArgumentException("No torrent url found")
        startStream(torrentMagneticUrl)

        val notification = NotificationCompat.Builder(this, TorApplication.TORRENT_DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Tor Movies")
            .setContentText(getString(R.string.preparing_stream))
            .setSmallIcon(R.drawable.ic_play)
            .build()

        startForeground(DOWNLOAD_NOTIFICATION_ID, notification)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = TorrentBinder()

    private fun startStream(torrentUrl: String) {
        val options = TorrentOptions.Builder()
            .removeFilesAfterStop(true)
            .autoDownload(true)
            .saveLocation(externalCacheDir)
            .build()
        currentStream = TorrentStream.init(options).apply { addListener(this@TorrentDownloadForegroundService) }
        currentStream?.startStream(torrentUrl)
    }

    override fun onStreamReady(torrent: Torrent?) {
        Log.d("TorMoviesLog", "onStreamReady: ${torrent!!.videoFile}")
    }

    override fun onStreamPrepared(torrent: Torrent?) {
        Log.d("TorMoviesLog", "OnStreamPrepared")
    }

    override fun onStreamStopped() {
        Log.d("TorMoviesLog", "onStreamStopped")
        stopSelf()
    }

    override fun onStreamStarted(torrent: Torrent?) {
        Log.d("TorMoviesLog", "onStreamStarted")
    }

    override fun onStreamProgress(torrent: Torrent?, status: StreamStatus?) {
        Log.d("TorMoviesLog", "Progress: ${status?.progress} | Speed: ${status?.downloadSpeed}")

        val message = "${getString(R.string.downloading_torrent)} ${status?.downloadSpeed?.toMBytes()?.formatMBytes() ?: "-"}"
        updateNotification(message)
    }

    override fun onStreamError(torrent: Torrent?, e: Exception?) {
        Log.e("TorMoviesLog", "Got error: $e")
        stopSelf()
    }

    private fun updateNotification(message: String) {
        val newNotification = NotificationCompat.Builder(this@TorrentDownloadForegroundService, TorApplication.TORRENT_DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Tor Movies")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_play)
            .build()

        NotificationManagerCompat.from(this@TorrentDownloadForegroundService).notify(DOWNLOAD_NOTIFICATION_ID, newNotification)
    }

    inner class TorrentBinder : Binder() {
        fun addTorrentListener(listener: TorrentListener) {
            currentStream?.addListener(listener)
        }
    }
}