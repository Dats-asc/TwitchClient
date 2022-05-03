package com.example.twitchclient.domain.usecases.bttvffz

import com.example.twitchclient.domain.entity.emotes.bttv.BttvFfzGlobalEmotes
import com.example.twitchclient.domain.repository.BttvFfzRepository
import javax.inject.Inject

class GetBttvGlobalEmotesUseCase @Inject constructor(
    private val bttvFfzRepository: BttvFfzRepository
) {

    suspend operator fun invoke(): BttvFfzGlobalEmotes {
        return bttvFfzRepository.getBttvGlobalEmotes()
    }
}