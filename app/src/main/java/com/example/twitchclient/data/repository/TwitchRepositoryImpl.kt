package com.example.twitchclient.data.repository

import com.example.twitchclient.C
import com.example.twitchclient.data.api.TwitchApi
import com.example.twitchclient.data.api.mapper.TwitchMapper
import com.example.twitchclient.data.responses.twitch.emotes.TwitchGlobalEmotesResponse
import com.example.twitchclient.domain.entity.emotes.TwitchGlobalEmotes
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
    private val accessToken: String?
) : TwitchRepository {

    private val BASE_URL = "https://api.twitch.tv/helix/"

    private val AUTH_QUERY_PARAMETER = "Authorization"
    private val CLIENT_ID_QUERY_PARAMETER = "Client-Id"

    private val authInterceptor = Interceptor { chain ->
        chain.run {
            val updatedRequestUrl = request().url.newBuilder()
                .build()

            proceed(
                request().newBuilder()
                    .url(updatedRequestUrl)
                    .addHeader(CLIENT_ID_QUERY_PARAMETER, C.CLIENT_ID)
                    .addHeader(AUTH_QUERY_PARAMETER, "Bearer $accessToken")
                    .build()
            )
        }
    }

    private val okhttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    private val api: TwitchApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TwitchApi::class.java)
    }

    override suspend fun pingUser(): User {
        return twitchMapper.mapUserResponse(api.pingUser())
    }

    override suspend fun getUserById(id: String): User {
        return twitchMapper.mapUserResponse(api.getUserById(id))
    }

    override suspend fun getUserByLogin(login: String): User {
        return twitchMapper.mapUserResponse(api.getUserByLogin(login))
    }

    override suspend fun getFollowedStreams(userId: String): Streams {
        return twitchMapper.mapStreamResponse(api.getFollowedStreams(userId))
    }

    override suspend fun getTwitchGlobalEmotes() : TwitchGlobalEmotes {
        return twitchMapper.mapTwitchGlobalEmotesResponse(api.getTwitchGlobalEmotes())
    }

}