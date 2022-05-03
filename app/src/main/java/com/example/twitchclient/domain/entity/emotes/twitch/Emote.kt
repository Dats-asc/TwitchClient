package com.example.twitchclient.domain.entity.emotes.twitch

import com.example.twitchclient.data.responses.twitch.emotes.Images

data class Emote(
    val format: List<String>,
    val id: String,
    val images: Images,
    val name: String,
    val scale: List<String>,
    val theme_mode: List<String>
)