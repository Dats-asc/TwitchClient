package com.example.twitchclient.domain.entity.videos

data class Videos(
    val videos: ArrayList<VideoInfo>,
    val cursor: String
)
