package com.example.twitchclient.data.api

import com.example.twitchclient.data.responses.usher.VideoPlaylistTokenResponse
import com.google.gson.JsonArray
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface GraphQLApi {

    @POST(".")
    suspend fun getVideoAccessToken(
        @Header("Client-ID") clientId: String?,
        @HeaderMap headers: Map<String, String>,
        @Body json: JsonArray
    ): VideoPlaylistTokenResponse
}