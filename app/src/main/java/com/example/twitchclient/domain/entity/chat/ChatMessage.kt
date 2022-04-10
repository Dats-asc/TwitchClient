package com.example.twitchclient.domain.entity.chat

data class ChatMessage(
    val id: Int = 0,
    val username: String,
    val message: String
)