package com.example.twitchclient.data.responses.games

data class GamesResponse(
    val `data`: List<Data>,
    val pagination: Pagination
)