package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.repository.TwitchRepository

class GetFollowedStreamsUseCase(
    val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(userId: String): Streams {
        return twitchRepository.getFollowedStreams(userId)
    }
}