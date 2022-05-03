package com.example.twitchclient.ui.followings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.usecases.twitch.GetFollowedStreamsUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

class FollowingsViewModel @Inject constructor(
    private val getFollowedStreamsUseCase: GetFollowedStreamsUseCase
) : ViewModel() {


    var savedData: Streams? = null

    private var _queryFollowedStreams: MutableLiveData<Result<Streams>> = MutableLiveData()
    val queryStreams: LiveData<Result<Streams>> = _queryFollowedStreams

    fun getFollowedStreams(){
        viewModelScope.launch {
            try {
                val followedStreams = getFollowedStreamsUseCase.invoke("211258578")
                    _queryFollowedStreams.value = Result.success(followedStreams)
            } catch (e: Exception){
                _queryFollowedStreams.value = Result.failure(e)
            }
        }
    }

}