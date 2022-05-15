package com.example.twitchclient.data.responses.twitch.channels

data class SearchChannelsResponse(
    val `data`: List<Data>,
    val pagination: Pagination
)