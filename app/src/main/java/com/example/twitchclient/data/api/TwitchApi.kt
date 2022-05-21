package com.example.twitchclient.data.api

import com.example.twitchclient.data.responses.games.GameResponse
import com.example.twitchclient.data.responses.games.GamesResponse
import com.example.twitchclient.data.responses.twitch.channels.SearchChannelsResponse
import com.example.twitchclient.data.responses.twitch.emotes.TwitchGlobalEmotesResponse
import com.example.twitchclient.data.responses.twitch.stream.StreamsResponse
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import com.example.twitchclient.data.responses.twitch.videos.VideosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchApi {

    @GET("/helix/users")
    suspend fun pingUser(): UserResponse

    @GET("/helix/users")
    suspend fun getUserById(@Query("id") id: String): UserResponse

    @GET("/helix/users")
    suspend fun getUsersByIds(@Query("id") ids: ArrayList<String>): UserResponse

    @GET("/helix/users")
    suspend fun getUserByLogin(@Query("login") login: String): UserResponse

    @GET("/helix/streams/followed")
    suspend fun getFollowedStreams(@Query("user_id") userId: String): StreamsResponse

    @GET("/helix/streams")
    suspend fun getStreams(@Query("after") cursor: String?): StreamsResponse

    @GET("/helix/games")
    suspend fun getGame(@Query("id") id: String): GameResponse

    @GET("/helix/streams")
    suspend fun getStreamsByGame(
        @Query("game_id") gameId: String,
        @Query("after") cursor: String?
    ): StreamsResponse

    @GET("/helix/streams")
    suspend fun getStreamsById(@Query("user_id") userId: String): StreamsResponse

    @GET("/helix/chat/emotes/global")
    suspend fun getTwitchGlobalEmotes(): TwitchGlobalEmotesResponse

    @GET("/helix/search/channels")
    suspend fun getChannelsByRequest(@Query("query") query: String): SearchChannelsResponse

    @GET("/helix/search/channels")
    suspend fun getChannelsAfter(
        @Query("query") query: String,
        @Query("after") cursor: String
    ): SearchChannelsResponse

    @GET("/helix/games/top")
    suspend fun getTopGames(@Query("after") cursor: String?): GamesResponse

    @GET("/helix/search/categories")
    suspend fun getGamesByRequest(
        @Query("query") query: String,
        @Query("after") cursor: String?
    ): GamesResponse

    @GET("/helix/videos")
    suspend fun getChannelVideos(
        @Query("user_id") userId: String,
        @Query("after") cursor: String?
    ): VideosResponse

}