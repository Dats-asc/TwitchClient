package com.example.twitchclient.data.responses.bttv

data class BttvChanelEmotesResponse(
    val avatar: String,
    val bots: List<String>,
    val channelEmotes: List<ChannelEmote>,
    val id: String,
    val sharedEmotes: List<SharedEmote>
)