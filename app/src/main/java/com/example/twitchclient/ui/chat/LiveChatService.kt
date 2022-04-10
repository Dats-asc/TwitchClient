package com.example.twitchclient.ui.chat

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.twitchclient.C
import okhttp3.*

class LiveChatService() : Service() {

    private lateinit var client: OkHttpClient

    private lateinit var webSocket: WebSocket

    private val DEFAULT_CLOSE_CODE = 0

    inner class LocaleBinder : Binder() {

        fun getWebSocket() = webSocket

        fun start(listener: WebSocketListener) {
            this@LiveChatService.start(listener)
        }

        fun sendMessage(msg: String, channelName: String){
            this@LiveChatService.sendMessage(msg, channelName)
        }

        fun stop() = this@LiveChatService.stop()

    }

    override fun onBind(intent: Intent?): IBinder? = LocaleBinder()

    private fun start(listener: WebSocketListener) {
        client = OkHttpClient()
        val request =
            Request.Builder()
                .url(C.TWITCH_CHAT_WSS_SERVER)
                .build()
        webSocket = client.newWebSocket(request, listener)
    }

    private fun sendMessage(msg: String, channelName: String){
        webSocket.send("PRIVMSG #$channelName :$msg")
    }

    private fun stop(){
        webSocket.close(DEFAULT_CLOSE_CODE, "")
        this.stopSelf()
    }

}