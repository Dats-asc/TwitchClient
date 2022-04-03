package com.example.twitchclient.data.api

import com.example.twitchclient.data.responses.twitch.stream.StreamsResponse
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchApi {

    @GET("https://api.twitch.tv/helix/users")
    suspend fun pingUser() : UserResponse

    @GET("https://api.twitch.tv/helix/users")
    suspend fun getUserById(@Query("id") id: String) : UserResponse

    @GET("https://api.twitch.tv/helix/users")
    suspend fun getUserByLogin(@Query("login") login: String) : UserResponse

    @GET("https://api.twitch.tv/helix/streams/followed")
    suspend fun getFollowedStreams(@Query("user_id") userId: String) : StreamsResponse

}