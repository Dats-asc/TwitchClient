package com.example.twitchclient.ui.search.games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.search.GameInfo
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.usecases.twitch.GetGamesByRequestUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class GamesTabViewModel @Inject constructor(
    private val getGamesByRequestUseCase: GetGamesByRequestUseCase
) : ViewModel() {

    private val allGames = arrayListOf<GameInfo>()

    private var lastCursor: String? = null

    private var lastRequest = ""

    private var _queryGames: MutableLiveData<Result<Games>> = MutableLiveData()

    private var _games: MutableLiveData<Result<ArrayList<GameInfo>>> = MutableLiveData()
    val games: LiveData<Result<ArrayList<GameInfo>>> = _games

    init {
        _queryGames.observeForever {
            it.fold(
                onSuccess = { gamesResponse ->
                    allGames.addAll(gamesResponse.games)
                    _games.value = Result.success(allGames)
                }, onFailure = {
                    _games.value = Result.failure(it)
                }
            )
        }
    }

    fun getGames(request: String) {
        allGames.clear()
        viewModelScope.launch {
            try {
                val followedStreams = getGamesByRequestUseCase(request, null)
                _queryGames.value = Result.success(followedStreams)
                lastCursor = followedStreams.cursor
                lastRequest = request
            } catch (e: Exception) {
                _queryGames.value = Result.failure(e)
            }
        }
    }

    fun getNextGames() {
        viewModelScope.launch {
            try {
                val followedStreams = getGamesByRequestUseCase(lastRequest, lastCursor)
                _queryGames.value = Result.success(followedStreams)
                lastCursor = followedStreams.cursor
            } catch (e: Exception) {
                _queryGames.value = Result.failure(e)
            }
        }
    }
}