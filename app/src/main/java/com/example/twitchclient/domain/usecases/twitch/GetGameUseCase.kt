package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetGameUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(id: String): Games {
        return twitchRepository.getGame(id)
    }
}