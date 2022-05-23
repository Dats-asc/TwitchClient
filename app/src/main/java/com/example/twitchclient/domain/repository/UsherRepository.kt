package com.example.twitchclient.domain.repository

import com.example.twitchclient.domain.entity.videos.VideoPlaylist
import okhttp3.ResponseBody
import retrofit2.Response

interface UsherRepository {

    suspend fun loadVideoPlaylist(gqlclientId: String, videoId: String): VideoPlaylist
}