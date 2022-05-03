package com.example.twitchclient.domain.entity.emotes

import com.example.twitchclient.domain.entity.emotes.bttv.BttvFfzEmote
import com.example.twitchclient.domain.entity.emotes.twitch.Emote

data class ChatEmotes(
    val twitchGlobalEmotes: List<Emote>,
    val bttvChannelEmotes: List<BttvFfzEmote>,
    val ffzChannelEmotes: List<BttvFfzEmote>
)