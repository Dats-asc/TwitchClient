package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetTwitchGlobalEmotesUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(): TwitchGlobalEmotes {
        return twitchRepository.getTwitchGlobalEmotes()
    }
}