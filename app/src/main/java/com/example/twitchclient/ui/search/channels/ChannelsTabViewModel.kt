package com.example.twitchclient.ui.search.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.search.ChannelInfo
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.usecases.twitch.GetChannelsAfterUseCase
import com.example.twitchclient.domain.usecases.twitch.GetChannelsByRequestUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ChannelsTabViewModel @Inject constructor(
    private val getChannelsByRequestUseCase: GetChannelsByRequestUseCase,
    private val getChannelsAfterUseCase: GetChannelsAfterUseCase
) : ViewModel() {

    private var lastCursor: String? = null

    private var lastRequest = ""

    private var allChannels = arrayListOf<ChannelInfo>()

    private var _channels: MutableLiveData<Result<ArrayList<ChannelInfo>>> = MutableLiveData()
    val channels: LiveData<Result<ArrayList<ChannelInfo>>> = _channels

    private var _queryChannels: MutableLiveData<Result<Channels>> = MutableLiveData()

    init {
        _queryChannels.observeForever {
            it.fold(
                onSuccess = { streamsResponse ->
                    allChannels.addAll(streamsResponse.channels)
                    _channels.value = Result.success(allChannels)
                }, onFailure = {
                    _channels.value = Result.failure(it)
                }
            )
        }
    }

    fun getChannels(request: String) {
        allChannels.clear()
        viewModelScope.launch {
            try {
                val followedStreams = getChannelsByRequestUseCase(request)
                _queryChannels.value = Result.success(followedStreams)
                lastCursor = followedStreams.cursor
                lastRequest = request
            } catch (e: Exception) {
                _queryChannels.value = Result.failure(e)
            }
        }
    }

    fun getNextChannels() {
        viewModelScope.launch {
            try {
                val followedStreams = getChannelsAfterUseCase(lastRequest, lastCursor.orEmpty())
                _queryChannels.value = Result.success(followedStreams)
                lastCursor = followedStreams.cursor
            } catch (e: Exception) {
                _queryChannels.value = Result.failure(e)
            }
        }
    }
}