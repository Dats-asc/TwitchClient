package com.example.twitchclient.data.room

import com.example.twitchclient.data.room.entity.VideoEntity
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.Videos
import java.util.*
import kotlin.collections.ArrayList

class DatabaseMapper {

    fun mapVideoInfo(videoInfo: VideoInfo): VideoEntity {
        return VideoEntity(
            id = videoInfo.id,
            userId = videoInfo.userId,
            userLogin = videoInfo.userLogin,
            title = videoInfo.title,
            description = videoInfo.description,
            savedAt = Calendar.getInstance().time.toString(),
            createdAt = videoInfo.createdAt,
            url = videoInfo.url,
            previewUrl = videoInfo.previewUrl,
            viewCount = videoInfo.viewCount,
            duration = videoInfo.duration
        )
    }

    fun mapVideoEntity(video: VideoEntity): VideoInfo {
        return VideoInfo(
            id = video.id,
            userId = video.userId,
            userLogin = video.userLogin,
            title = video.title,
            description = video.description,
            publishedAt = video.savedAt,
            createdAt = video.createdAt,
            url = video.url,
            previewUrl = video.previewUrl,
            viewCount = video.viewCount,
            duration = video.duration
        )
    }

    fun mapVideoEntityList(videoEntities: Array<VideoEntity>): Videos {
        val mappedList = arrayListOf<VideoInfo>()
        videoEntities.forEach { dbVideo ->
            mappedList.add(
                VideoInfo(
                    id = dbVideo.id,
                    userId = dbVideo.userId,
                    userLogin = dbVideo.userLogin,
                    title = dbVideo.title,
                    description = dbVideo.description,
                    publishedAt = dbVideo.savedAt,
                    createdAt = dbVideo.createdAt,
                    url = dbVideo.url,
                    previewUrl = dbVideo.previewUrl,
                    viewCount = dbVideo.viewCount,
                    duration = dbVideo.duration
                )
            )
        }
        return Videos(mappedList, "sdsadas")
    }
}