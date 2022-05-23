package com.example.twitchclient.ui.videos

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource

class VideoPlayerService : Service() {

    private lateinit var mediaSource: MediaSource

    private lateinit var player: ExoPlayer

    private lateinit var onPlayerCreated: (ExoPlayer) -> Unit

    private lateinit var videoUrl: String

    private var lastPlayerPosition = 0L

    inner class LocaleBinder : Binder() {

        fun getPlayer(): ExoPlayer {
            return this@VideoPlayerService.player
        }

        val isPlaying: Boolean get() = this@VideoPlayerService.player.isPlaying ?: false

        fun startPlayer(url: String, onPlayerCreated: (ExoPlayer) -> Unit) {
            this@VideoPlayerService.startPlayer(url, onPlayerCreated)
        }

        fun play() = this@VideoPlayerService.play()

        fun pause() = this@VideoPlayerService.pause()

        fun changeQuality(url: String) =
            this@VideoPlayerService.changeVideoQuality(url)

        fun stop() = this@VideoPlayerService.stopPlayer()
    }

    override fun onBind(intent: Intent): IBinder = LocaleBinder()

    private fun play() {
        player.playWhenReady = true
    }

    private fun pause() {
        player.playWhenReady = false
    }

    private fun stopPlayer() {
        player.stop()
        player.clearMediaItems()
        this.stopSelf()
    }

    private fun startPlayer(url: String, onPlayerCreated: (ExoPlayer) -> Unit) {
        this.onPlayerCreated = onPlayerCreated
        videoUrl = url
        initPlayer()
    }

    private fun changeVideoQuality(newUrl: String) {
        lastPlayerPosition = player.currentPosition
        videoUrl = newUrl
        createMediaSource(newUrl)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.seekTo(lastPlayerPosition)
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(this)
            .build()
            .also {
                onPlayerCreated.invoke(it)
            }
        createMediaSource(videoUrl)
        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
    }

    private fun createMediaSource(url: String) {
        player.seekTo(0)

        val httpDataSourceFactory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
                .setDefaultRequestProperties(mapOf("X-Donate-To" to "https://ttv.lol/donate"))

        val dataSourceFactory = DataSource.Factory {
            val dataSource: HttpDataSource = httpDataSourceFactory.createDataSource()
            dataSource
        }

        mediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .createMediaSource(
                MediaItem.Builder()
                    .setUri(Uri.parse(url))
                    .build()
            )
    }
}