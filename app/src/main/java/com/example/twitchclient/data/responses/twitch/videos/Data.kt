package com.example.twitchclient.data.responses.twitch.videos

data class Data(
    val created_at: String,
    val description: String,
    val duration: String,
    val id: String,
    val language: String,
    val muted_segments: Any,
    val published_at: String,
    val stream_id: String,
    val thumbnail_url: String,
    val title: String,
    val type: String,
    val url: String,
    val user_id: String,
    val user_login: String,
    val user_name: String,
    val view_count: Int,
    val viewable: String
)