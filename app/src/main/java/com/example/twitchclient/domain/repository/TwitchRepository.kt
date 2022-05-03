package com.example.twitchclient.domain.repository

import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.User

interface TwitchRepository {

    suspend fun pingUser(): User

    suspend fun getUserById(id: String): User

    suspend fun getUserByLogin(login: String): User

    suspend fun getFollowedStreams(userId: String): Streams

    suspend fun getTwitchGlobalEmotes(): TwitchGlobalEmotes
}