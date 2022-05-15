package com.example.twitchclient.domain.entity.search

data class Games(
    val games: ArrayList<GameInfo>,
    val cursor: String
)