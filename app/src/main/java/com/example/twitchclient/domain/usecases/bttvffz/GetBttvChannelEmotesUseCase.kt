package com.example.twitchclient.domain.usecases.bttvffz

import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.repository.BttvFfzRepository
import javax.inject.Inject

class GetBttvChannelEmotesUseCase @Inject constructor(
    private val bttvFfzRepository: BttvFfzRepository
) {

    suspend operator fun invoke(userId: String): BttvChanelEmotes {
        return bttvFfzRepository.getBttvChannelEmotes(userId)
    }
}