package com.example.twitchclient.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val userLogin: String,
    val title: String,
    val description: String,
    val savedAt: String,
    val createdAt: String,
    val url: String,
    val previewUrl: String,
    val viewCount: Int,
    val duration: String,
    var hlsUrl: String?
)
