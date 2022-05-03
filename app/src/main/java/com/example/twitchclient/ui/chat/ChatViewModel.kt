package com.example.twitchclient.ui.chat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.twitchclient.domain.entity.chat.ChatMessage
import com.example.twitchclient.domain.entity.emotes.ChatEmotes
import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.entity.emotes.ffz.FfzChannelEmotes
import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes
import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.usecases.bttvffz.GetBttvChannelEmotesUseCase
import com.example.twitchclient.domain.usecases.bttvffz.GetFfzChannelEmotesUseCase
import com.example.twitchclient.domain.usecases.twitch.GetTwitchGlobalEmotesUseCase
import com.example.twitchclient.domain.usecases.twitch.PingUserUseCase
import kotlinx.coroutines.launch
import okhttp3.*
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val context: Context,
    private val accessToken: String?,
    private val pingUserUseCase: PingUserUseCase,
    private val getTwitchGlobalEmotesUseCase: GetTwitchGlobalEmotesUseCase,
    private val getBttvChannelEmotesUseCase: GetBttvChannelEmotesUseCase,
    private val getFfzChannelEmotesUseCase: GetFfzChannelEmotesUseCase
) : ViewModel() {

    private var liveChatServiceBinder: LiveChatService.LocaleBinder? = null

    private lateinit var broadcasterLogin: String

    private var broadcasterId = "22484632"

    private lateinit var onChatLoaded: () -> Unit

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

    private var userLiveData: MutableLiveData<Result<User>> = MutableLiveData()

    private var user: User? = null

    private var messagesCount = 0

    private var _queryChatMessages: MutableLiveData<Result<ChatMessage>> = MutableLiveData()
    val queryChatMessages: LiveData<Result<ChatMessage>> = _queryChatMessages

    private var data: Triple<Result<FfzChannelEmotes>, Result<TwitchGlobalEmotes>, Result<BttvChanelEmotes>>? =
        null

    private var _emotes: MutableLiveData<Result<Triple<Result<FfzChannelEmotes>, Result<TwitchGlobalEmotes>, Result<BttvChanelEmotes>>>> =
        MutableLiveData()

    val emotes: LiveData<Result<Triple<Result<FfzChannelEmotes>, Result<TwitchGlobalEmotes>, Result<BttvChanelEmotes>>>> =
        _emotes

    private var _chatEmotes: MutableLiveData<Result<ChatEmotes>> = MutableLiveData()
    val chatEmotes: LiveData<Result<ChatEmotes>> = _chatEmotes

    fun startChat(
        broadcasterLogin: String,
        onChatLoaded: () -> Unit
    ) {
        this.broadcasterLogin = broadcasterLogin
        this.onChatLoaded = onChatLoaded
        initData()
        emotes.observeForever() {
            bindChatService()
        }
    }

    private fun onWebSocketMessage(ws: WebSocket, msg: String) {
        if (msg.contains("PING :tmi.twitch.tv")) {
            ws.send("PONG :tmi.twitch.tv")
            return
        }
        if (messagesCount == 0) {
            ws.send("JOIN #$broadcasterLogin")
            messagesCount++
        } else if (messagesCount == 1 || messagesCount == 2) {
            messagesCount++
            onChatLoaded.invoke()
        } else {
            val chatMessage = msg.replace("\n", "").replace("\r", "")
            val username = chatMessage.substringAfter(':').substringBefore('!')
            val message = chatMessage.substringAfter(':').substringAfter(':')
            _queryChatMessages.postValue(Result.success(ChatMessage(0, username, message)))
        }
    }

    fun sendMessage(msg: String) {
        liveChatServiceBinder?.sendMessage(msg, broadcasterLogin)
    }

    fun stopChat() = liveChatServiceBinder?.stop()

    private fun initData() {
        viewModelScope.launch {
            try {
                val userResponse = pingUserUseCase.invoke()
                val twitchGlobalEmotesResponse = getTwitchGlobalEmotesUseCase.invoke()
                val bttvChanelEmotesResponse = getBttvChannelEmotesUseCase.invoke(broadcasterId)
                val ffzChannelEmotes = getFfzChannelEmotesUseCase.invoke(broadcasterId)

                _chatEmotes.value = Result.success(ChatEmotes(
                    twitchGlobalEmotes = twitchGlobalEmotesResponse.emotes,
                    bttvChannelEmotes = bttvChanelEmotesResponse.channelEmotes,
                    ffzChannelEmotes = ffzChannelEmotes.channelEmotes
                ))

                data = Triple(
                    Result.success(ffzChannelEmotes),
                    Result.success(twitchGlobalEmotesResponse),
                    Result.success(bttvChanelEmotesResponse)
                )
                this@ChatViewModel.user = userResponse
                _emotes.value = Result.success(data!!)
            } catch (e: Exception) {
                Log.e("ChatViewModel", e.message.toString())
                _emotes.value = Result.failure(e)
            }
        }
    }

    private fun onWebSocketOpen(ws: WebSocket) {
        ws.send("PASS oauth:$accessToken")
        ws.send("NICK ${user?.login ?: ""}")
    }

    private fun bindChatService() {
        context.bindService(
            Intent(context, LiveChatService::class.java),
            connection,
            AppCompatActivity.BIND_AUTO_CREATE
        )
    }
}