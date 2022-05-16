package com.example.twitchclient.data.responses.twitch.stream

data class Data(
    val game_id: String,
    val game_name: String,
    val id: String,
    val is_mature: Boolean,
    val language: String,
    val started_at: String,
    val tag_ids: List<String>,
    val thumbnail_url: String,
    val title: String,
    val type: String,
    val user_id: String,
    val user_login: String,
    val user_name: String,
    val viewer_count: Int
)