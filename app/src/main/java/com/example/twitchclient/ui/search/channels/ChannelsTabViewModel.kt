package com.example.twitchclient.ui.search.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.usecases.twitch.GetChannelsAfterUseCase
import com.example.twitchclient.domain.usecases.twitch.GetChannelsByRequestUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelsTabViewModel @Inject constructor(
    private val getChannelsByRequestUseCase: GetChannelsByRequestUseCase,
    private val getChannelsAfterUseCase: GetChannelsAfterUseCase
) : ViewModel() {

    private var lastRequest: String? = null

    private var lastCursor: String? = null

    private var _queryChannels: MutableLiveData<Result<Channels>> = MutableLiveData()
    val queryChannels: LiveData<Result<Channels>> = _queryChannels

    private var _queryNextChannels: MutableLiveData<Result<Channels>> = MutableLiveData()
    val queryNextChannels: LiveData<Result<Channels>> = _queryNextChannels

    fun onFirstQuery(request: String) {
        viewModelScope.launch {
            try {
                val channels = getChannelsByRequestUseCase(request)
                _queryChannels.value = Result.success(channels)
                lastRequest = request
            } catch (e: Exception) {
                _queryChannels.value = Result.failure(e)
            }
        }

    }

    fun onNextChannels() {
        viewModelScope.launch {
            try {
                val channels = getChannelsAfterUseCase(lastRequest.orEmpty(), lastCursor.orEmpty())
                _queryNextChannels.value = Result.success(channels)
                lastCursor = channels.cursor
            } catch (e: Exception) {
                _queryNextChannels.value = Result.failure(e)
            }
        }
    }


}