package com.example.twitchclient.domain.usecases.bttvffz

import com.example.twitchclient.domain.entity.emotes.GeneralEmotes
import com.example.twitchclient.domain.repository.BttvFfzRepository
import javax.inject.Inject

class GetGeneralEmotesUseCase @Inject constructor(
    private val bttvFfzRepository: BttvFfzRepository

) {
    suspend operator fun invoke(userId: String): GeneralEmotes {
        return bttvFfzRepository.getEmotes(userId)
    }
}