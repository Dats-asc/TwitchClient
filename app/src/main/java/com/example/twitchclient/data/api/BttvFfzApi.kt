package com.example.twitchclient.data.api

import com.example.twitchclient.data.responses.bttv.BttvChanelEmotesResponse
import com.example.twitchclient.data.responses.bttv.BttvGlobalEmotesResponse
import com.example.twitchclient.data.responses.ffz.FfzChannelEmotesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BttvFfzApi {

    @GET("https://api.betterttv.net/3/cached/emotes/global")
    suspend fun getBttvGlobalEmotes(): BttvGlobalEmotesResponse

    @GET("https://api.betterttv.net/3/cached/users/twitch/{user_id}")
    suspend fun getBttvChannelEmotes(@Path("user_id") userId: String): BttvChanelEmotesResponse

    @GET("https://api.betterttv.net/3/cached/frankerfacez/users/twitch/{user_id}")
    suspend fun getFfzChannelEmotes(@Path("user_id") userId: String): FfzChannelEmotesResponse
}