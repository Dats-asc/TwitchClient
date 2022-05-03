package com.example.twitchclient.data.responses.twitch.emotes

data class Data(
    val format: List<String>,
    val id: String,
    val images: Images,
    val name: String,
    val scale: List<String>,
    val theme_mode: List<String>
)