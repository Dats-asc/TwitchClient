package com.example.twitchclient.ui.search.games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.usecases.twitch.GetGamesByRequestUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GamesTabViewModel @Inject constructor(
    private val getGamesByRequestUseCase: GetGamesByRequestUseCase
) : ViewModel() {

    private var lastRequest: String? = null

    private var lastCursor: String? = null

    private var _queryGames: MutableLiveData<Result<Games>> = MutableLiveData()
    val queryGames: LiveData<Result<Games>> = _queryGames

    private var _queryNextGames: MutableLiveData<Result<Games>> = MutableLiveData()
    val queryNextGames: LiveData<Result<Games>> = _queryNextGames

    fun onFirstQuery(request: String) {
        viewModelScope.launch {
            try {
                val channels = getGamesByRequestUseCase(request, null)
                _queryGames.value = Result.success(channels)
                lastRequest = request
            } catch (e: Exception) {
                _queryGames.value = Result.failure(e)
            }
        }

    }

    fun onNextChannels() {
        viewModelScope.launch {
            try {
                val channels = getGamesByRequestUseCase(lastRequest.orEmpty(), lastCursor.orEmpty())
                _queryNextGames.value = Result.success(channels)
                lastCursor = channels.cursor
            } catch (e: Exception) {
                _queryNextGames.value = Result.failure(e)
            }
        }
    }
}