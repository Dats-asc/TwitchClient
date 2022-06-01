package com.example.twitchclient.domain.entity.emotes

data class EmoteGeneral(
    val code: String,
    val id: String,
    val imageType: String,
    val url1x: String,
    val url2x: String,
    val url3x: String
)