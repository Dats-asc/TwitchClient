package com.example.twitchclient.ui.chat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.chat.ChatMessage
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.usecases.twitch.PingUserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.internal.notifyAll
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val context: Context,
    private val accessToken: String?,
    private val pingUserUseCase: PingUserUseCase
) : ViewModel() {

    private var liveChatServiceBinder: LiveChatService.LocaleBinder? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            liveChatServiceBinder = service as? LiveChatService.LocaleBinder
            liveChatServiceBinder?.start(chatListener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            liveChatServiceBinder = null
        }
    }

    private val chatListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            onWebSocketOpen(webSocket)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            onWebSocketMessage(webSocket, text)
        }
    }

    private lateinit var user: User

    private var messagesCount = 0

    private var _queryChatMessages: MutableLiveData<Result<ChatMessage>> = MutableLiveData()
    val queryChatMessages: LiveData<Result<ChatMessage>> = _queryChatMessages

    fun startChat() {
        initUser()
        bindChatService()
    }

    private fun onWebSocketMessage(ws: WebSocket, msg: String) {
        if (messagesCount == 0) {
            ws.send("JOIN #aimlul")
            messagesCount++
        } else if (messagesCount == 1 || messagesCount == 2) {
            messagesCount++
        } else {
            val username = msg.substringAfter(':').substringBefore('!')
            val message = msg.substringAfter(':').substringAfter(':')
            val chatMessage = ChatMessage(0, username, message)
            _queryChatMessages.postValue(Result.success(chatMessage))
        }
    }

    fun sendMessage(msg: String){
        liveChatServiceBinder?.sendMessage(msg, "uebermarginal")
    }

    private fun onWebSocketOpen(ws: WebSocket) {
        ws.send("PASS oauth:$accessToken")
        ws.send("NICK guest005")
    }

    private fun bindChatService() {
        context.bindService(
            Intent(context, LiveChatService::class.java),
            connection,
            AppCompatActivity.BIND_AUTO_CREATE
        )
    }

    private fun initUser() {
        viewModelScope.launch {
            user = pingUserUseCase.invoke()
        }
    }

}