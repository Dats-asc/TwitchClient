package com.example.twitchclient.domain.entity.search

data class ChannelInfo(
    val broadcaster_login: String,
    val display_name: String,
    val game_id: String,
    val id: String,
    val is_live: Boolean,
    val thumbnail_url: String,
)