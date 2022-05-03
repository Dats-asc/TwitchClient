package com.example.twitchclient.data.responses.ffz

data class FfzChannelEmotesResponseItem(
    val code: String,
    val id: Int,
    val imageType: String,
    val images: Images,
    val user: User
)