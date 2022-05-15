package com.example.twitchclient.domain.entity.search

data class Channels(
    val channels: ArrayList<ChannelInfo>,
    val cursor: String
)