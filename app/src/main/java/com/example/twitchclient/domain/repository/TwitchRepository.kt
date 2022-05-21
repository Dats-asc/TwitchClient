package com.example.twitchclient.domain.repository

import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.Token
import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.entity.user.UserDetail
import com.example.twitchclient.domain.entity.videos.Videos

interface TwitchRepository {

    suspend fun pingUser(): User

    fun putAccessToken(token: Token)

    fun getAccessToken(): Token

    fun deleteToken()

    suspend fun getUserById(id: String): User

    suspend fun getUserByLogin(login: String): User

    suspend fun getFollowedStreams(userId: String): Streams

    suspend fun getTwitchGlobalEmotes(): TwitchGlobalEmotes

    suspend fun getChannelsByRequest(request: String): Channels

    suspend fun getStreams(cursor: String?): Streams

    suspend fun getSteamsByGame(gameId: String, cursor: String?): Streams

    suspend fun getGame(id: String): Games

    suspend fun getTopGames(cursor: String?): Games

    suspend fun getChannelsAfter(request: String, after: String): Channels

    suspend fun getGamesByRequest(request: String, cursor: String?): Games

    suspend fun getUserDetail(userId: String): UserDetail

    suspend fun getChannelVideos(userId: String, cursor: String?): Videos
}