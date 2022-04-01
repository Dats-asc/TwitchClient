package com.example.twitchclient.domain.entity.user

import com.example.twitchclient.data.responses.twitch.user.Data
import com.example.twitchclient.data.responses.twitch.user.UserResponse

interface TwitchRepository {

    suspend fun pingUser(): UserResponse

    suspend fun getUserById(id: Int): Data

    suspend fun getUserByLogin(login: String): Data
}