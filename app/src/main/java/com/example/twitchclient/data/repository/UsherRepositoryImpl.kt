package com.example.twitchclient.data.repository

import android.util.Log
import com.example.twitchclient.data.api.GraphQLApi
import com.example.twitchclient.data.api.UsherApi
import com.example.twitchclient.data.responses.usher.VideoPlaylistTokenResponse
import com.example.twitchclient.domain.entity.videos.VideoPlaylist
import com.example.twitchclient.domain.repository.UsherRepository
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class UsherRepositoryImpl @Inject constructor(
    private val usherApi: UsherApi,
    private val graphQLApi: GraphQLApi
) : UsherRepository {

    override suspend fun loadVideoPlaylist(gqlclientId: String, videoId: String): VideoPlaylist {
        val body = withContext(
            Dispatchers.IO
        ) {
            Log.d("", "Getting video playlist for video $videoId")
            val accessToken = loadVideoPlaylistAccessToken(gqlclientId, videoId)
            val playlistQueryOptions = HashMap<String, String>()
            playlistQueryOptions["token"] = accessToken.token
            playlistQueryOptions["sig"] = accessToken.signature
            playlistQueryOptions["allow_source"] = "true"
            playlistQueryOptions["allow_audio_only"] = "true"
            playlistQueryOptions["type"] = "any"
            playlistQueryOptions["p"] = Random.nextInt(999999).toString()
            usherApi.getVideoPlaylist(videoId, playlistQueryOptions)
        }
        val playlist = body.body()!!.string()
        val qualities =
            "NAME=\"(.*)\"".toRegex().findAll(playlist).map { it.groupValues[1] }.toMutableList()
        val urls =
            "https://.*\\.m3u8".toRegex().findAll(playlist).map(MatchResult::value).toMutableList()
        return VideoPlaylist(urls, qualities)

    }

    private suspend fun loadVideoPlaylistAccessToken(
        gqlclientId: String,
        videoId: String
    ): VideoPlaylistTokenResponse {
        //        val accessToken = api.getVideoAccessToken(clientId, id, token)
        val accessTokenJson = getAccessTokenJson(
            isLive = false,
            isVod = true,
            login = "",
            playerType = "channel_home_live",
            vodId = videoId
        )
        val accessTokenHeaders = getAccessTokenHeaders()
        // accessTokenHeaders["Authorization"] = ""
        val a = 0
        return graphQLApi.getVideoAccessToken(gqlclientId, accessTokenHeaders, accessTokenJson)
    }

    private fun getAccessTokenHeaders(
        randomDeviceId: Boolean = true,
        xdeviceid: String = "",
        deviceid: String = ""
    ): MutableMap<String, String> {
        return HashMap<String, String>().apply {
            if (randomDeviceId) {
                val randomid = UUID.randomUUID().toString().replace("-", "").substring(
                    0,
                    32
                ) //X-Device-Id or Device-ID removes "commercial break in progress" (length 16 or 32)
                put("X-Device-Id", randomid)
                put("Device-ID", randomid)
            } else {
                if (xdeviceid != "")
                    put("X-Device-Id", xdeviceid)
                if (deviceid != "")
                    put("Device-ID", deviceid)
            }
            put("Accept", "*/*")
            put("Accept-Encoding", "gzip, deflate, br")
            put("Accept-Language", "ru-RU")
            put("Connection", "keep-alive")
            put("Content-Type", "text/plain;charset=UTF-8")
            put("Host", "gql.twitch.tv")
            put("Origin", "https://www.twitch.tv")
            put("Referer", "https://www.twitch.tv/")
            put(
                "sec-ch-ua",
                "\"Google Chrome\";v=\"87\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"87\""
            )
            put("sec-ch-ua-mobile", "?0")
            put("Sec-Fetch-Dest", "empty")
            put("Sec-Fetch-Mode", "cors")
            put("Sec-Fetch-Site", "same-site")
            put(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36"
            )
        }
    }

    private fun getAccessTokenJson(
        isLive: Boolean,
        isVod: Boolean,
        login: String,
        playerType: String,
        vodId: String
    ): JsonArray {
        val jsonArray = JsonArray(1)
        val operation = JsonObject().apply {
            addProperty("operationName", "PlaybackAccessToken")
            add("variables", JsonObject().apply {
                addProperty("isLive", isLive)
                addProperty("isVod", isVod)
                addProperty("login", login)
                addProperty("playerType", playerType)
                addProperty("vodID", vodId)
            })
            add("extensions", JsonObject().apply {
                add("persistedQuery", JsonObject().apply {
                    addProperty("version", 1)
                    addProperty(
                        "sha256Hash",
                        "0828119ded1c13477966434e15800ff57ddacf13ba1911c129dc2200705b0712"
                    )
                })
            })
        }
        jsonArray.add(operation)
        return jsonArray
    }
}