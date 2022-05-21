package com.example.twitchclient.ui.followings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.StreamItem
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.usecases.twitch.DeleteAccessTokenUseCase
import com.example.twitchclient.domain.usecases.twitch.GetAccessTokenUseCase
import com.example.twitchclient.domain.usecases.twitch.GetFollowedStreamsUseCase
import com.example.twitchclient.domain.usecases.twitch.GetUserByIdUseCase
import com.example.twitchclient.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

class FollowingsViewModel @Inject constructor(
    private val getFollowedStreamsUseCase: GetFollowedStreamsUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val deleteAccessTokenUseCase: DeleteAccessTokenUseCase
) : ViewModel() {

    val isAuthorized get() = !getAccessTokenUseCase().isNullOrEmpty()

    private var _queryFollowedStreams: MutableLiveData<Result<Streams>> = MutableLiveData()
    val queryStreams: LiveData<Result<Streams>> = _queryFollowedStreams

    private var _streams: MutableLiveData<Result<ArrayList<StreamItem>>> = MutableLiveData()
    val streams: LiveData<Result<ArrayList<StreamItem>>> = _streams

    fun getFollowedStreams() {
        viewModelScope.launch {
            try {
                val followedStreams = getFollowedStreamsUseCase.invoke("211258578")
                _queryFollowedStreams.value = Result.success(followedStreams)
            } catch (e: Exception) {
                _queryFollowedStreams.value = Result.failure(e)
            }
        }
    }

    fun logout() = deleteAccessTokenUseCase()
}