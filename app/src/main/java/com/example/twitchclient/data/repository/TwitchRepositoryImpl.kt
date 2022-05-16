package com.example.twitchclient.data.repository

import com.example.twitchclient.C
import com.example.twitchclient.data.api.TwitchApi
import com.example.twitchclient.data.api.mapper.TwitchMapper
import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.repository.TwitchRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class TwitchRepositoryImpl @Inject constructor(
    private val twitchMapper: TwitchMapper,
    private val accessToken: String?,
    private val twitchApi: TwitchApi
) : TwitchRepository {

    private val BASE_URL = "https://api.twitch.tv/helix/"

    private val AUTH_QUERY_PARAMETER = "Authorization"
    private val CLIENT_ID_QUERY_PARAMETER = "Client-Id"

    override suspend fun pingUser(): User {
        return twitchMapper.mapUserResponse(twitchApi.pingUser())
    }

    override suspend fun getUserById(id: String): User {
        return twitchMapper.mapUserResponse(twitchApi.getUserById(id))
    }

    override suspend fun getUserByLogin(login: String): User {
        return twitchMapper.mapUserResponse(twitchApi.getUserByLogin(login))
    }

    override suspend fun getFollowedStreams(userId: String): Streams {
        return twitchMapper.mapStreamResponse(twitchApi.getFollowedStreams(userId))
    }

    override suspend fun getTwitchGlobalEmotes(): TwitchGlobalEmotes {
        return twitchMapper.mapTwitchGlobalEmotesResponse(twitchApi.getTwitchGlobalEmotes())
    }

    override suspend fun getChannelsByRequest(request: String): Channels {
        return twitchMapper.mapSearchChannelResponse(twitchApi.getChannelsByRequest(request))
    }

    override suspend fun getStreams(cursor: String?): Streams {
        return twitchMapper.mapStreamResponse(twitchApi.getStreams(cursor))
    }

    override suspend fun getChannelsAfter(request: String, after: String): Channels {
        return twitchMapper.mapSearchChannelResponse(twitchApi.getChannelsAfter(request, after))
    }

    override suspend fun getGamesByRequest(request: String, cursor: String?): Games {
        return twitchMapper.mapGamesResponse(twitchApi.getGamesByRequest(request, cursor))
    }

}