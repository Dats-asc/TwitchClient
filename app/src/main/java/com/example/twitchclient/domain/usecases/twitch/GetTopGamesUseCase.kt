package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetTopGamesUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(cursor: String?): Games {
        return twitchRepository.getTopGames(cursor)
    }
}