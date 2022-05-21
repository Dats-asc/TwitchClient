package com.example.twitchclient.ui.channel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.user.UserDetail
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.Videos
import com.example.twitchclient.domain.usecases.twitch.GetChannelVideosUseCase
import com.example.twitchclient.domain.usecases.twitch.GetUserDetailUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ChannelDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getChannelVideosUseCase: GetChannelVideosUseCase
) : ViewModel() {

    private var _userDetail: MutableLiveData<Result<UserDetail>> = MutableLiveData()
    val userDetail: LiveData<Result<UserDetail>> = _userDetail

    private var lastCursor: String? = null

    private val allVideos = arrayListOf<VideoInfo>()

    private var _queryVideos: MutableLiveData<Result<Videos>> = MutableLiveData()

    private var _videos: MutableLiveData<Result<ArrayList<VideoInfo>>> = MutableLiveData()
    val videos: LiveData<Result<ArrayList<VideoInfo>>> = _videos

    init {
        _queryVideos.observeForever {
            it.fold(onSuccess = { videosResponse ->
                allVideos.addAll(videosResponse.videos)
                _videos.value = Result.success(videosResponse.videos)
            }, onFailure = {
                _videos.value = Result.failure(it)
            }
            )
        }
    }

    fun getChannelVideos(userId: String) {
        viewModelScope.launch {
            try {
                val videos = getChannelVideosUseCase(userId, null)
                _queryVideos.value = Result.success(videos)
                lastCursor = videos.cursor
            } catch (e: Exception) {
                _queryVideos.value = Result.failure(e)
            }
        }
    }

    fun getNextVideos(userId: String) {
        viewModelScope.launch {
            try {
                val videos = getChannelVideosUseCase(userId, lastCursor)
                _queryVideos.value = Result.success(videos)
                lastCursor = videos.cursor
            } catch (e: Exception) {
                _queryVideos.value = Result.failure(e)
            }
        }
    }


    fun getUserDetail(userId: String) {
        viewModelScope.launch {
            try {
                val userDetail = getUserDetailUseCase(userId)
                _userDetail.value = Result.success(userDetail)
            } catch (e: Exception) {

            }
        }
    }
}