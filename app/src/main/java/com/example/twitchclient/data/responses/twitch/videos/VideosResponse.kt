package com.example.twitchclient.data.responses.twitch.videos

data class VideosResponse(
    val `data`: List<Data>,
    val pagination: Pagination
)