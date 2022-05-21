package com.example.twitchclient.data.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.twitchclient.C
import com.example.twitchclient.data.api.TwitchApi
import com.example.twitchclient.data.api.mapper.TwitchMapper
import com.example.twitchclient.data.responses.twitch.stream.StreamsResponse
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.entity.user.Token
import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.entity.user.UserDetail
import com.example.twitchclient.domain.entity.videos.Videos
import com.example.twitchclient.domain.repository.TwitchRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.inject.Inject

class TwitchRepositoryImpl @Inject constructor(
    private val twitchMapper: TwitchMapper,
    private val twitchApi: TwitchApi,
    private val context: Context
) : TwitchRepository {

    private val BASE_URL = "https://api.twitch.tv/helix/"

    private val AUTH_QUERY_PARAMETER = "Authorization"
    private val CLIENT_ID_QUERY_PARAMETER = "Client-Id"

    override suspend fun pingUser(): User {
        return twitchMapper.mapUserResponse(twitchApi.pingUser())
    }

    override fun putAccessToken(token: Token) {
        context.getSharedPreferences(C.USER_PREFERENCES, Context.MODE_PRIVATE).edit()
            .putString(C.USER_ACCESS_TOKEN_VALUE, token.accessToken)
            .apply()
    }

    override fun getAccessToken(): Token {
        val token = context.getSharedPreferences(C.USER_PREFERENCES, Context.MODE_PRIVATE)
            .getString(C.USER_ACCESS_TOKEN_VALUE, "")
        return Token(token.orEmpty())
    }

    override fun deleteToken() {
        context.getSharedPreferences(C.USER_PREFERENCES, Context.MODE_PRIVATE).edit()
            .remove(C.USER_ACCESS_TOKEN_VALUE)
            .apply()
    }

    override suspend fun getUserById(id: String): User {
        return twitchMapper.mapUserResponse(twitchApi.getUserById(id))
    }

    override suspend fun getUserByLogin(login: String): User {
        return twitchMapper.mapUserResponse(twitchApi.getUserByLogin(login))
    }

    override suspend fun getFollowedStreams(userId: String): Streams {
        val ids = arrayListOf<String>()
        var ids2 = ""
        val streams = twitchApi.getFollowedStreams(userId)
        streams.data.forEach { stream ->
            ids.add(stream.user_id)
            ids2 += "id=${stream.user_id}&"
        }
        val users = twitchApi.getUsersByIds(ids)
        return twitchMapper.mapStreamResponse(streams, users)
    }

    override suspend fun getTwitchGlobalEmotes(): TwitchGlobalEmotes {
        return twitchMapper.mapTwitchGlobalEmotesResponse(twitchApi.getTwitchGlobalEmotes())
    }

    override suspend fun getChannelsByRequest(request: String): Channels {
        return twitchMapper.mapSearchChannelResponse(twitchApi.getChannelsByRequest(request))
    }

    override suspend fun getStreams(cursor: String?): Streams {
        val ids = arrayListOf<String>()
        val streams = twitchApi.getStreams(cursor)
        streams.data.forEach { stream ->
            ids.add(stream.user_id)
        }
        val users = twitchApi.getUsersByIds(ids)
        return twitchMapper.mapStreamResponse(streams, users)
    }

    override suspend fun getSteamsByGame(gameId: String, cursor: String?): Streams {
        val ids = arrayListOf<String>()
        val streams = twitchApi.getStreamsByGame(gameId, cursor)
        streams.data.forEach { stream ->
            ids.add(stream.user_id)
        }
        val users = twitchApi.getUsersByIds(ids)
        return twitchMapper.mapStreamResponse(streams, users)
    }

    override suspend fun getGame(id: String): Games {
        return twitchMapper.mapGameResponse(twitchApi.getGame(id))
    }

    override suspend fun getTopGames(cursor: String?): Games {
        return twitchMapper.mapGamesResponse(twitchApi.getTopGames(cursor))
    }

    override suspend fun getChannelsAfter(request: String, after: String): Channels {
        return twitchMapper.mapSearchChannelResponse(twitchApi.getChannelsAfter(request, after))
    }

    override suspend fun getGamesByRequest(request: String, cursor: String?): Games {
        return twitchMapper.mapGamesResponse(twitchApi.getGamesByRequest(request, cursor))
    }

    override suspend fun getUserDetail(userId: String): UserDetail {
        val user = twitchApi.getUserById(userId)
        val channelInfo = twitchApi.getStreamsById(userId)
        return twitchMapper.mapUserDetail(user, channelInfo)
    }

    override suspend fun getChannelVideos(userId: String, cursor: String?): Videos {
        return twitchMapper.mapVideosResponse(twitchApi.getChannelVideos(userId, cursor))
    }

}