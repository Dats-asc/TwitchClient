package com.example.twitchclient.data.api

import com.example.twitchclient.data.responses.games.GamesResponse
import com.example.twitchclient.data.responses.twitch.channels.SearchChannelsResponse
import com.example.twitchclient.data.responses.twitch.emotes.TwitchGlobalEmotesResponse
import com.example.twitchclient.data.responses.twitch.stream.StreamsResponse
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchApi {

    @GET("/helix/users")
    suspend fun pingUser(): UserResponse

    @GET("/helix/users")
    suspend fun getUserById(@Query("id") id: String): UserResponse

    @GET("/helix/users")
    suspend fun getUserByLogin(@Query("login") login: String): UserResponse

    @GET("/helix/streams/followed")
    suspend fun getFollowedStreams(@Query("user_id") userId: String): StreamsResponse

    @GET("/helix/chat/emotes/global")
    suspend fun getTwitchGlobalEmotes(): TwitchGlobalEmotesResponse

    @GET("/helix/search/channels")
    suspend fun getChannelsByRequest(@Query("query") query: String): SearchChannelsResponse

    @GET("/helix/search/channels")
    suspend fun getChannelsAfter(
        @Query("query") query: String,
        @Query("after") cursor: String
    ): SearchChannelsResponse

    @GET("/helix/search/categories")
    suspend fun getGamesByRequest(
        @Query("query") query: String,
        @Query("after") cursor: String?
    ): GamesResponse
}