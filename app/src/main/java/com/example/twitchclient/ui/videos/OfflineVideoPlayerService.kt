package com.example.twitchclient.ui.videos

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

class OfflineVideoPlayerService : Service() {

    private lateinit var videoCache: SimpleCache
    private lateinit var mediaSource: MediaSource

    private lateinit var player: ExoPlayer

    private lateinit var onPlayerCreated: (ExoPlayer) -> Unit

    private lateinit var videoUrl: String

    private var lastPlayerPosition = 0L

    private var currentVideo: VideoInfo? = null

    inner class LocaleBinder : Binder() {

        fun getPlayer(): ExoPlayer {
            return this@OfflineVideoPlayerService.player
        }

        val isPlaying: Boolean get() = this@OfflineVideoPlayerService.player.isPlaying ?: false

        fun startPlayer(
            video: VideoInfo,
            onPlayerCreated: (ExoPlayer) -> Unit
        ) {
            this@OfflineVideoPlayerService.startPlayer(video, onPlayerCreated)
        }

        fun play() = this@OfflineVideoPlayerService.play()

        fun pause() = this@OfflineVideoPlayerService.pause()

        fun changeQuality(url: String) =
            this@OfflineVideoPlayerService.changeVideoQuality(url)

        fun stop() = this@OfflineVideoPlayerService.stopPlayer()
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

    private fun startPlayer(
        video: VideoInfo,
        onPlayerCreated: (ExoPlayer) -> Unit
    ) {
        this.onPlayerCreated = onPlayerCreated
        currentVideo = video
        initPlayer()
    }

    private fun changeVideoQuality(newUrl: String) {
        lastPlayerPosition = player.currentPosition
        videoUrl = newUrl
        player.setMediaSource(mediaSource)
        player.prepare()
        player.seekTo(lastPlayerPosition)
    }

    private fun initPlayer() {
        val cache = DownloadUtils.getVideoCache(this)

        val cacheSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
            .setCacheWriteDataSinkFactory(null)

        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheSourceFactory))
            .build()
            .also {
                onPlayerCreated.invoke(it)
            }
        player.setMediaItem(
            DownloadRequest.Builder(
                currentVideo?.id.orEmpty(),
                Uri.parse(currentVideo?.hlsUrl)
            ).build().toMediaItem()
        )
        player.prepare()
        player.playWhenReady = true
    }
}