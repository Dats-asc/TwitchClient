package com.example.twitchclient.ui.videos

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.VideoPlaylist
import com.example.twitchclient.domain.usecases.video.DeleteVideoUseCase
import com.example.twitchclient.domain.usecases.video.GetAllVideosUseCase
import com.example.twitchclient.ui.stream.PlayerService
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideosViewModel @Inject constructor(
    private val getAllVideosUseCase: GetAllVideosUseCase,
    private val deleteVideoUseCase: DeleteVideoUseCase
) : ViewModel() {

    private val allVideos = arrayListOf<VideoInfo>()

    private var _videos: MutableLiveData<ArrayList<VideoInfo>> = MutableLiveData()
    val videos: LiveData<ArrayList<VideoInfo>> = _videos

    fun getSavedVideos() {
        viewModelScope.launch {
            try {
                val videos = getAllVideosUseCase()
                _videos.value = videos.videos
                allVideos.addAll(videos.videos)
            } catch (e: Exception) {
                Log.e("", e.message.orEmpty())
            }
        }
    }

    fun deleteVideo(id: String) {
        viewModelScope.launch {
            deleteVideoUseCase(id)
            allVideos.remove(allVideos.first { video -> video.id == id })
            _videos.value = allVideos
        }
    }
}