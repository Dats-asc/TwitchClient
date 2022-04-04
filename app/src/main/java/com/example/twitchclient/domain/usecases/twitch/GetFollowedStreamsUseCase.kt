package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetFollowedStreamsUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(userId: String): Streams {
        return twitchRepository.getFollowedStreams(userId)
    }
}