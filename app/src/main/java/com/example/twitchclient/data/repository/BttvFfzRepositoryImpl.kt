package com.example.twitchclient.data.repository

import com.example.twitchclient.data.api.BttvFfzApi
import com.example.twitchclient.data.api.TwitchApi
import com.example.twitchclient.data.api.mapper.BttvFfzMapper
import com.example.twitchclient.domain.entity.emotes.GeneralEmotes
import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.entity.emotes.bttv.BttvFfzGlobalEmotes
import com.example.twitchclient.domain.entity.emotes.ffz.FfzChannelEmotes
import com.example.twitchclient.domain.repository.BttvFfzRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BttvFfzRepositoryImpl @Inject constructor(
    private val bttvFfzApi: BttvFfzApi,
    private val bttvFfzMapper: BttvFfzMapper,
    private val twitchApi: TwitchApi
) : BttvFfzRepository {

    private val bttvScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override suspend fun getBttvGlobalEmotes(): BttvFfzGlobalEmotes {
        return withContext(bttvScope.coroutineContext) {
            bttvFfzMapper.mapBttvGlobalEmotesResponse(bttvFfzApi.getBttvGlobalEmotes())
        }
    }

    override suspend fun getBttvChannelEmotes(userId: String): BttvChanelEmotes {
        return withContext(bttvScope.coroutineContext) {
            bttvFfzMapper.mapBttvChannelEmotesResponse(bttvFfzApi.getBttvChannelEmotes(userId))
        }
    }

    override suspend fun getFfzChannelEmotes(userId: String): FfzChannelEmotes {
        return withContext(bttvScope.coroutineContext) {
            bttvFfzMapper.mapFfzChannelEmotesResponse(bttvFfzApi.getFfzChannelEmotes(userId))
        }
    }

    override suspend fun getEmotes(userId: String): GeneralEmotes {
        return withContext(bttvScope.coroutineContext) {
            val twitchGlobalEmotes = twitchApi.getTwitchGlobalEmotes()
            val bttvGlobalEmotes = bttvFfzApi.getBttvGlobalEmotes()
            val bttvChannelEmotes = bttvFfzApi.getBttvChannelEmotes(userId)
            val ffzChannelEmotes = bttvFfzApi.getFfzChannelEmotes(userId)
            bttvFfzMapper.mapEmotes(
                twitchGlobalEmotes,
                bttvGlobalEmotes,
                bttvChannelEmotes,
                ffzChannelEmotes
            )
        }
    }


}