package com.example.twitchclient.data.room.repository

import com.example.twitchclient.data.room.DatabaseMapper
import com.example.twitchclient.data.room.VideoDao
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.Videos
import com.example.twitchclient.domain.repository.RoomRepository
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val videoDao: VideoDao,
    private val databaseMapper: DatabaseMapper
) : RoomRepository {


    override suspend fun addVideo(video: VideoInfo) {
        if (!videoDao.videoExist(video.id)) {
            videoDao.addVideo(databaseMapper.mapVideoInfo(video))
        }
    }

    override suspend fun getAllVideos(): Videos {
        return databaseMapper.mapVideoEntityList(videoDao.getAllVideos())
    }

    override suspend fun getVideo(id: String): VideoInfo {
        return databaseMapper.mapVideoEntity(videoDao.getVideo(id))
    }

    override suspend fun deleteVideo(id: String) {
        return videoDao.deleteVideo(id)
    }

    override suspend fun videoSaved(id: String): Boolean {
        return videoDao.videoExist(id)
    }


}