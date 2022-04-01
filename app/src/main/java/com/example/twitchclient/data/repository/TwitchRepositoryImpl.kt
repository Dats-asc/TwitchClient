package com.example.twitchclient.data.repository

import com.example.twitchclient.Constants
import com.example.twitchclient.data.api.TwitchApi
import com.example.twitchclient.data.responses.twitch.user.Data
import com.example.twitchclient.data.responses.twitch.user.UserResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TwitchRepositoryImpl(
    userAccessToken: String
) : TwitchApi {

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
                    .addHeader(CLIENT_ID_QUERY_PARAMETER, Constants.CLIENT_ID)
                    .addHeader(AUTH_QUERY_PARAMETER, "Bearer ${userAccessToken}")
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

    override suspend fun pingUser(): UserResponse {
        return api.pingUser()
    }

    override suspend fun getUserById(id: Int): Data {
        return api.getUserById(id)
    }

    override suspend fun getUserByLogin(login: String): Data {
        return api.getUserByLogin(login)
    }

}