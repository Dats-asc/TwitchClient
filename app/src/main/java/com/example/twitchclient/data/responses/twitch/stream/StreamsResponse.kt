package com.example.twitchclient.data.responses.twitch.stream

data class StreamsResponse(
    val `data`: List<Data>,
    val pagination: Pagination
)