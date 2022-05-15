package com.example.twitchclient.data.responses.twitch.channels

data class Data(
    val broadcaster_language: String,
    val broadcaster_login: String,
    val display_name: String,
    val game_id: String,
    val game_name: String,
    val id: String,
    val is_live: Boolean,
    val started_at: String,
    val tag_ids: List<String>,
    val thumbnail_url: String,
    val title: String
)