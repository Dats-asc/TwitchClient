package com.example.twitchclient.ui.stream

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistParserFactory
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistTracker
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.*

class PlayerService : Service() {

    private lateinit var mediaSource: MediaSource

    private lateinit var player: ExoPlayer

    private var trackSelector = DefaultTrackSelector()

    private lateinit var broadcasterLogin: String

    private lateinit var onPlayerCreated: (ExoPlayer) -> Unit

    inner class LocaleBinder : Binder() {

        fun getPlayer(): ExoPlayer {
            return this@PlayerService.player
        }

        fun isPlaying() = this@PlayerService.player.isPlaying

        fun startPlayer(broadcasterLogin: String, onPlayerCreated: (ExoPlayer) -> Unit) {
            this@PlayerService.startPlayer(broadcasterLogin, onPlayerCreated)
        }

        fun play() = this@PlayerService.play()

        fun pause() = this@PlayerService.pause()

        fun restart() = this@PlayerService.restart()

        fun changeQuality(qualityOption: Int) = this@PlayerService.changeVideoQuality(qualityOption)

        fun getMappedQualityList() = this@PlayerService.getQualityOptions()

        fun stop() = this@PlayerService.stopPlayer()
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

    private fun restart() {
        with(player) {
            clearMediaItems()
            setMediaSource(mediaSource)
            prepare()
        }
    }

    private fun startPlayer(broadcasterLogin: String, onPlayerCreated: (ExoPlayer) -> Unit) {
        this.broadcasterLogin = broadcasterLogin
        this.onPlayerCreated = onPlayerCreated
        initPlayer()
    }

    private fun changeVideoQuality(qualityOption: Int) {
        trackSelector.currentMappedTrackInfo?.let {
            if (qualityOption != 0) {
                trackSelector.parameters = trackSelector.buildUponParameters()
                    .setSelectionOverride(
                        0,
                        it.getTrackGroups(0),
                        DefaultTrackSelector.SelectionOverride(0, qualityOption - 1)
                    )

                    .build()
            } else {
                trackSelector.parameters = DefaultTrackSelector().buildUponParameters()
                    .setAllowMultipleAdaptiveSelections(true)
                    .build()
            }
        }
    }

    private fun getQualityOptions(): List<String> {
        val mappedTrackInfo = trackSelector.currentMappedTrackInfo
        var mappedQualityList = mutableListOf("Автоматически")
        var qualityArray = mappedTrackInfo?.getTrackGroups(0)?.get(0)
        qualityArray?.let { qualityArray ->
            for (i in 0 until qualityArray.length) {
                val format = qualityArray.getFormat(i)
                mappedQualityList.add("${format.height}p${format.frameRate}")
            }
        }
        return mappedQualityList
    }

    private fun initPlayer() {
        player = ExoPlayer.Builder(this)
            .setTrackSelector(trackSelector)
            .build()
        createMediaSource()
        player.setMediaSource(mediaSource)
        player.prepare()
        player.playWhenReady = true
        onPlayerCreated.invoke(player)
    }

    private fun createMediaSource() {
        val httpDataSourceFactory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
                .setDefaultRequestProperties(mapOf("X-Donate-To" to "https://ttv.lol/donate"))

        val dataSourceFactory = DataSource.Factory {
            val dataSource: HttpDataSource = httpDataSourceFactory.createDataSource()
            dataSource
        }

        mediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .setPlaylistParserFactory(DefaultHlsPlaylistParserFactory())
            .setPlaylistTrackerFactory(DefaultHlsPlaylistTracker.FACTORY)
            .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(6))
            .createMediaSource(
                MediaItem.Builder()
                    .setUri(Uri.parse("https://api.ttv.lol/playlist/$broadcasterLogin.m3u8%3Fallow_source=true&allow_audio_only=true&type=any&p=211724&fast_bread=true&player_backend=mediaplayer&supported_codecs=avc1&player_version=1.4.0&warp=true"))
                    .build()
            )
    }
}