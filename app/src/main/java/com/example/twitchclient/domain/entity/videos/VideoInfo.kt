package com.example.twitchclient.domain.entity.videos

data class VideoInfo(
    val id: String,
    val userId: String,
    val userLogin: String,
    val title: String,
    val description: String,
    val createdAt: String,
    val publishedAt: String,
    val url: String,
    val previewUrl: String,
    val viewCount: Int,
    val duration: String,

)
