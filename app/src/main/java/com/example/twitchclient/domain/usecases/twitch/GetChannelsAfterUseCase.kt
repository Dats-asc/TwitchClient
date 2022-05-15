package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetChannelsAfterUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(request: String, cursor: String): Channels {
        return twitchRepository.getChannelsAfter(request, cursor)
    }
}