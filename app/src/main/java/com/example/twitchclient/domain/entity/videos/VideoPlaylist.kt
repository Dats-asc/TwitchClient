package com.example.twitchclient.domain.entity.videos

data class VideoPlaylist(
    val urls: MutableList<String>,
    val qualities: MutableList<String>
)
