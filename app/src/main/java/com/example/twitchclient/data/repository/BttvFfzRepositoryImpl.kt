package com.example.twitchclient.data.repository

import com.example.twitchclient.data.api.BttvFfzApi
import com.example.twitchclient.data.api.mapper.BttvFfzMapper
import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.entity.emotes.bttv.BttvFfzGlobalEmotes
import com.example.twitchclient.domain.entity.emotes.ffz.FfzChannelEmotes
import com.example.twitchclient.domain.repository.BttvFfzRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class BttvFfzRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val bttvFfzMapper: BttvFfzMapper
) : BttvFfzRepository {

    private val BASE_URL = "https://api.betterttv.net/3/"

    private val api: BttvFfzApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BttvFfzApi::class.java)
    }

    override suspend fun getBttvGlobalEmotes(): BttvFfzGlobalEmotes {
        return bttvFfzMapper.mapBttvGlobalEmotesResponse(api.getBttvGlobalEmotes())
    }

    override suspend fun getBttvChannelEmotes(userId: String): BttvChanelEmotes {
        return bttvFfzMapper.mapBttvChannelEmotesResponse(api.getBttvChannelEmotes(userId))
    }

    override suspend fun getFfzChannelEmotes(userId: String): FfzChannelEmotes {
        return bttvFfzMapper.mapFfzChannelEmotesResponse(api.getFfzChannelEmotes(userId))
    }


}