package com.example.twitchclient.domain.repository

import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.Videos

interface RoomRepository {

    suspend fun addVideo(video: VideoInfo)

    suspend fun getAllVideos(): Videos

    suspend fun getVideo(id: String): VideoInfo

    suspend fun deleteVideo(id: String)

    suspend fun videoSaved(id: String): Boolean
}