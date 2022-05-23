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
import com.example.twitchclient.domain.entity.videos.VideoPlaylist
import com.example.twitchclient.domain.repository.UsherRepository
import com.example.twitchclient.ui.videos.VideoPlayerService
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class VideoViewModel @Inject constructor(
    private val context: Context,
    private val usherRepository: UsherRepository
) : ViewModel() {

    private var videoPlayerServiceBinder: VideoPlayerService.LocaleBinder? = null

    var videoPlaylist: MutableLiveData<VideoPlaylist> = MutableLiveData()

    var videoPlayer: MutableLiveData<ExoPlayer> = MutableLiveData()

    private lateinit var currentUrl: String


    private val connection = object : ServiceConnection {
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

    init {
        videoPlaylist.observeForever {
            currentUrl = it.urls[0]
            startPlayer()
        }
    }

    fun start(videoId: String) {
        viewModelScope.launch {
            try {
                videoPlaylist.value = usherRepository.loadVideoPlaylist(C.GQL_CLIENT_ID, videoId)
            } catch (e: Exception) {
                Log.e("", e.message.orEmpty())
            }
        }
    }

    val isPlaying get() = videoPlayerServiceBinder?.isPlaying

    fun stop() = videoPlayerServiceBinder?.stop()

    fun pause() = videoPlayerServiceBinder?.pause()

    fun play() = videoPlayerServiceBinder?.play()

    fun setQualityOption(url: String) = videoPlayerServiceBinder?.changeQuality(url)

    private fun startPlayer() {
        bindService()
    }

    private fun bindService() {
        context.bindService(
            Intent(context, VideoPlayerService::class.java),
            connection,
            AppCompatActivity.BIND_AUTO_CREATE
        )
    }
}