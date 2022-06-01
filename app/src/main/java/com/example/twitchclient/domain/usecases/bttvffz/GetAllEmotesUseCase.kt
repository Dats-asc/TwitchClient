package com.example.twitchclient.domain.usecases.bttvffz

import com.example.twitchclient.domain.entity.emotes.EmotesGeneral
import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.repository.BttvFfzRepository
import javax.inject.Inject

class GetAllEmotesUseCase @Inject constructor(
    private val bttvFfzRepository: BttvFfzRepository
) {

    suspend operator fun invoke(userId: String): EmotesGeneral {
        return bttvFfzRepository.getEmotes(userId)
    }
}