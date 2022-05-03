package com.example.twitchclient.domain.entity.emotes.twitch


data class TwitchGlobalEmotes(
    val emotes: List<Emote>,
    val template: String
)