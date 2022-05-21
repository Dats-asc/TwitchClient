package com.example.twitchclient.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.search.GameInfo
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.usecases.twitch.GetGameUseCase
import com.example.twitchclient.domain.usecases.twitch.GetStreamsByGameUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val getGameUseCase: GetGameUseCase,
    private val getStreamsByGameUseCase: GetStreamsByGameUseCase
) : ViewModel() {

    private var _game: MutableLiveData<Result<GameInfo>> = MutableLiveData()
    val game: LiveData<Result<GameInfo>> = _game

    private lateinit var gameId: String

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

    fun getStreams(id: String) {
        gameId = id
        if (allStreams.isEmpty()) {
            viewModelScope.launch {
                try {
                    val followedStreams = getStreamsByGameUseCase(gameId, null)
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
                val followedStreams = getStreamsByGameUseCase(gameId, lastCursor)
                _queryStreams.value = Result.success(followedStreams)
                lastCursor = followedStreams.cursor
            } catch (e: Exception) {
                _queryStreams.value = Result.failure(e)
            }
        }
    }

    fun getGame(id: String) {
        gameId = id
        viewModelScope.launch {
            try {
                val game = getGameUseCase(id)
                _game.value = Result.success(game.games[0])
            } catch (e: Exception) {
                _game.value = Result.failure(e)
            }
        }
    }
}