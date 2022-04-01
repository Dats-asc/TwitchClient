package com.example.twitchclient.data.responses.twitch.stream

data class StreamResponse(
    val `data`: List<Data>,
    val pagination: Pagination
)