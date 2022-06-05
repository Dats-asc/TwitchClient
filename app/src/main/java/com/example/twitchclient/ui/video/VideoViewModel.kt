package com.example.twitchclient.ui.video

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.C
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.VideoPlaylist
import com.example.twitchclient.domain.repository.UsherRepository
import com.example.twitchclient.domain.usecases.video.GetVideoUseCase
import com.example.twitchclient.ui.videos.OfflineVideoPlayerService
import com.example.twitchclient.ui.videos.VideoPlayerService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val context: Context,
    private val usherRepository: UsherRepository,
) : ViewModel() {

    private var videoPlayerServiceBinder: VideoPlayerService.LocaleBinder? = null

    private var offlineVideoPlayerServiceBinder: OfflineVideoPlayerService.LocaleBinder? = null

    var videoPlaylist: MutableLiveData<VideoPlaylist> = MutableLiveData()

    var videoPlayer: MutableLiveData<ExoPlayer> = MutableLiveData()

    private var currentVideoInfo: VideoInfo? = null

    private lateinit var currentUrl: String


    private val videoServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            videoPlayerServiceBinder = service as? VideoPlayerService.LocaleBinder
            videoPlayerServiceBinder?.startPlayer(
                currentUrl,
                onPlayerCreated = { player ->
                    videoPlayer.value = player
                })
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            videoPlayerServiceBinder = null
        }
    }

    private val offlineVideoServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            offlineVideoPlayerServiceBinder = service as? OfflineVideoPlayerService.LocaleBinder
            offlineVideoPlayerServiceBinder?.startPlayer(
                currentVideoInfo!!,
                onPlayerCreated = { player ->
                    videoPlayer.value = player
                })
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            videoPlayerServiceBinder = null
        }
    }

    init {
        videoPlaylist.observeForever {
            currentUrl = it.urls[0]
            startPlayer()
        }
    }

    fun start(video: VideoInfo, playerType: PlayerType) {
        currentVideoInfo = video
        if (playerType == PlayerType.ONLINE) {
            viewModelScope.launch {
                try {
                    videoPlaylist.value =
                        usherRepository.loadVideoPlaylist(C.GQL_CLIENT_ID, video.id)
                } catch (e: Exception) {
                    Log.e("", e.message.orEmpty())
                }
            }
        } else {
            startOfflinePlayer()
        }
    }

    val isPlaying get() = videoPlayerServiceBinder?.isPlaying

    fun stop() {
        videoPlayerServiceBinder?.stop()
        offlineVideoPlayerServiceBinder?.stop()
    }

    fun pause() {
        videoPlayerServiceBinder?.pause()
        offlineVideoPlayerServiceBinder?.pause()
    }

    fun play() = videoPlayerServiceBinder?.play()

    fun setQualityOption(url: String) = videoPlayerServiceBinder?.changeQuality(url)

    private fun startPlayer() {
        bindVideoService()
    }

    private fun startOfflinePlayer() {
        bindOfflineVideoService()
    }

    private fun bindVideoService() {
        context.bindService(
            Intent(context, VideoPlayerService::class.java),
            videoServiceConnection,
            AppCompatActivity.BIND_AUTO_CREATE
        )
    }

    private fun bindOfflineVideoService() {
        context.bindService(
            Intent(context, OfflineVideoPlayerService::class.java),
            offlineVideoServiceConnection,
            AppCompatActivity.BIND_AUTO_CREATE
        )
    }
}

enum class PlayerType {
    OFFLINE,
    ONLINE
}