package com.example.twitchclient.domain.usecases.bttvffz

import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.entity.emotes.ffz.FfzChannelEmotes
import com.example.twitchclient.domain.repository.BttvFfzRepository
import javax.inject.Inject


class GetFfzChannelEmotesUseCase @Inject constructor(
    private val bttvFfzRepository: BttvFfzRepository
) {

    suspend operator fun invoke(userId: String): FfzChannelEmotes {
        return bttvFfzRepository.getFfzChannelEmotes(userId)
    }
}