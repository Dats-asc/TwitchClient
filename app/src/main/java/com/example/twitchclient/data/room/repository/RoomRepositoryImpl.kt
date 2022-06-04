package com.example.twitchclient.data.room.repository

import com.example.twitchclient.data.room.DatabaseMapper
import com.example.twitchclient.data.room.VideoDao
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.Videos
import com.example.twitchclient.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val videoDao: VideoDao,
    private val databaseMapper: DatabaseMapper
) : RoomRepository {

    private val roomScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun addVideo(video: VideoInfo) {
        withContext(roomScope.coroutineContext) {
            if (!videoDao.videoExist(video.id)) {
                videoDao.addVideo(databaseMapper.mapVideoInfo(video))
            }
        }
    }

    override suspend fun getAllVideos(): Videos {
        return withContext(roomScope.coroutineContext) {
            databaseMapper.mapVideoEntityList(videoDao.getAllVideos())
        }
    }

    override suspend fun getVideo(id: String): VideoInfo {
        return withContext(roomScope.coroutineContext) {
            databaseMapper.mapVideoEntity(videoDao.getVideo(id))
        }
    }

    override suspend fun deleteVideo(id: String) {
        return withContext(roomScope.coroutineContext) {
            videoDao.deleteVideo(id)
        }
    }

    override suspend fun videoSaved(id: String): Boolean {
        return withContext(roomScope.coroutineContext) {
            videoDao.videoExist(id)
        }
    }
}