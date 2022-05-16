package com.example.twitchclient.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.streams.StreamItem
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.usecases.twitch.GetStreamsUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class PopularViewModel @Inject constructor(
    private val getStreamsUseCase: GetStreamsUseCase
) : ViewModel() {

    private var lastCursor: String? = null

    private var _queryStreams: MutableLiveData<Result<Streams>> = MutableLiveData()
    val queryStreams: LiveData<Result<Streams>> = _queryStreams

    private var _nextStreams: MutableLiveData<Result<Streams>> = MutableLiveData()
    val nextStreams: LiveData<Result<Streams>> = _nextStreams

    fun getPopularStreams() {
        viewModelScope.launch {
            try {
                val followedStreams = getStreamsUseCase(null)
                _queryStreams.value = Result.success(followedStreams)
                lastCursor = followedStreams.cursor
            } catch (e: Exception) {
                _queryStreams.value = Result.failure(e)
            }
        }
    }

    fun getNextStreams(){
        viewModelScope.launch {
            try {
                val nextStreams = getStreamsUseCase(lastCursor)
                _nextStreams.value = Result.success(nextStreams)
                lastCursor = nextStreams.cursor
            } catch (e: Exception) {
                _nextStreams.value = Result.failure(e)
            }
        }
    }
}