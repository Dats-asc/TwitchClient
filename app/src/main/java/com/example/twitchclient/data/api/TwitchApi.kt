package com.example.twitchclient.data.api

import com.example.twitchclient.data.responses.twitch.user.Data
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import com.example.twitchclient.domain.entity.user.User
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchApi {

    @GET("https://api.twitch.tv/helix/users")
    suspend fun pingUser() : UserResponse

    @GET("https://api.twitch.tv/helix/users")
    suspend fun getUserById(@Query("id") id: Int) : Data

    @GET("https://api.twitch.tv/helix/users")
    suspend fun getUserByLogin(@Query("login") login: String) : Data

}