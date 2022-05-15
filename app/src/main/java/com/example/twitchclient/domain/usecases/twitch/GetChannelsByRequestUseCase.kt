package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetChannelsByRequestUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(request: String): Channels {
        return twitchRepository.getChannelsByRequest(request)
    }
}