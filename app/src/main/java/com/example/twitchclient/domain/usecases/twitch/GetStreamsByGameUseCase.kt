package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetStreamsByGameUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(gameId: String, cursor: String?): Streams {
        return twitchRepository.getSteamsByGame(gameId, cursor)
    }
}