package com.example.twitchclient.ui.stream

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.twitchclient.ui.chat.LiveChatService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import javax.inject.Inject

class StreamViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {

    private var streamPlayerServiceBinder: PlayerService.LocaleBinder? = null

    private lateinit var onServiceBind: () -> String

    private lateinit var onPlayerCreated: (ExoPlayer) -> Unit

    private lateinit var broadcasterLogin: String

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            streamPlayerServiceBinder = service as? PlayerService.LocaleBinder
            streamPlayerServiceBinder?.startPlayer(onServiceBind.invoke(), onPlayerCreated)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            streamPlayerServiceBinder = null
        }
    }

    fun start(onServiceBind: () -> String, onPlayerCreated: (ExoPlayer) -> Unit){
        this.onServiceBind = onServiceBind
        this.onPlayerCreated = onPlayerCreated
        bindService()
    }

    fun restart() = streamPlayerServiceBinder?.restart()

    fun play() = streamPlayerServiceBinder?.play()

    fun stop() = streamPlayerServiceBinder?.stop()

    fun getQualityOptionsList() = streamPlayerServiceBinder?.getMappedQualityList()

    fun setQualityOption(option: Int) = streamPlayerServiceBinder?.changeQuality(option)

    private fun bindService(){
        context.bindService(
            Intent(context, PlayerService::class.java),
            connection,
            AppCompatActivity.BIND_AUTO_CREATE
        )
    }
}