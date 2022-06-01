package com.example.twitchclient.data.repository

import com.example.twitchclient.data.api.BttvFfzApi
import com.example.twitchclient.data.api.mapper.BttvFfzMapper
import com.example.twitchclient.domain.entity.emotes.EmotesGeneral
import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.entity.emotes.bttv.BttvFfzGlobalEmotes
import com.example.twitchclient.domain.entity.emotes.ffz.FfzChannelEmotes
import com.example.twitchclient.domain.repository.BttvFfzRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class BttvFfzRepositoryImpl @Inject constructor(
    private val bttvApi: BttvFfzApi,
    private val bttvFfzMapper: BttvFfzMapper,
) : BttvFfzRepository {

    override suspend fun getBttvGlobalEmotes(): BttvFfzGlobalEmotes {
        return bttvFfzMapper.mapBttvGlobalEmotesResponse(bttvApi.getBttvGlobalEmotes())
    }

    override suspend fun getBttvChannelEmotes(userId: String): BttvChanelEmotes {
        return bttvFfzMapper.mapBttvChannelEmotesResponse(bttvApi.getBttvChannelEmotes(userId))
    }

    override suspend fun getFfzChannelEmotes(userId: String): FfzChannelEmotes {
        return bttvFfzMapper.mapFfzChannelEmotesResponse(bttvApi.getFfzChannelEmotes(userId))
    }

    override suspend fun getEmotes(userId: String): EmotesGeneral {
        val bttvGlobalEmotes = bttvApi.getBttvGlobalEmotes()
        val bttvChannelEmotes = bttvApi.getBttvChannelEmotes(userId)
        val ffzChannelEmotes = bttvApi.getFfzChannelEmotes(userId)
        return bttvFfzMapper.mapEmotes(bttvGlobalEmotes, bttvChannelEmotes, ffzChannelEmotes)
    }


}