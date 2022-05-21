package com.example.twitchclient.ui.popular

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.streams.StreamData
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

    private var allStreams = arrayListOf<StreamData>()

    private var _streams: MutableLiveData<Result<ArrayList<StreamData>>> = MutableLiveData()
    val streams: LiveData<Result<ArrayList<StreamData>>> = _streams

    private var _queryStreams: MutableLiveData<Result<Streams>> = MutableLiveData()

    init {
        _queryStreams.observeForever {
            it.fold(
                onSuccess = { streamsResponse ->
                    allStreams.addAll(streamsResponse.streams)
                    _streams.value = Result.success(allStreams)
                }, onFailure = {
                    _streams.value = Result.failure(it)
                }
            )
        }
    }

    fun getPopularStreams() {
        if (allStreams.isEmpty()) {
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
    }

    fun getNextStreams() {
        viewModelScope.launch {
            try {
                val followedStreams = getStreamsUseCase(lastCursor)
                _queryStreams.value = Result.success(followedStreams)
                lastCursor = followedStreams.cursor
            } catch (e: Exception) {
                _queryStreams.value = Result.failure(e)
            }
        }
    }
}