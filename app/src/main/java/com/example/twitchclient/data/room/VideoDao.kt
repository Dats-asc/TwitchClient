package com.example.twitchclient.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.twitchclient.data.room.entity.VideoEntity

@Dao
interface VideoDao {

    @Transaction
    @Query("SELECT * FROM videos")
    suspend fun getAllVideos(): Array<VideoEntity>

    @Transaction
    @Insert
    suspend fun addVideo(videoEntity: VideoEntity)

    @Transaction
    @Query("SELECT * from videos WHERE id = :id")
    suspend fun getVideo(id: String): VideoEntity

    @Query("DELETE FROM videos WHERE id = :id")
    suspend fun deleteVideo(id: String)

    @Query("SELECT EXISTS(SELECT * FROM videos WHERE id = :id)")
    suspend fun videoExist(id: String): Boolean
}