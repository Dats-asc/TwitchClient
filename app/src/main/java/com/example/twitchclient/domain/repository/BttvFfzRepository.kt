package com.example.twitchclient.domain.repository

import com.example.twitchclient.domain.entity.emotes.EmotesGeneral
import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.entity.emotes.bttv.BttvFfzGlobalEmotes
import com.example.twitchclient.domain.entity.emotes.ffz.FfzChannelEmotes

interface BttvFfzRepository {

    suspend fun getBttvGlobalEmotes(): BttvFfzGlobalEmotes

    suspend fun getBttvChannelEmotes(userId: String): BttvChanelEmotes

    suspend fun getFfzChannelEmotes(userId: String): FfzChannelEmotes

    suspend fun getEmotes(userId: String): EmotesGeneral
}