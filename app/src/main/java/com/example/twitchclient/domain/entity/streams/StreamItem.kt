package com.example.twitchclient.domain.entity.streams

data class StreamItem(
    val id: String,
    val user_id: String,
    val user_login: String,
    val user_name: String,
    val game_id: String,
    val game_name: String,
    val type: String,
    val title: String,
    val viewer_count: Int,
    val started_at: String,
    val language: String,
    val thumbnail_url: String,
    val user_icon: String,
)