package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.videos.Videos
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetChannelVideosUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(userId: String, cursor: String?): Videos {
        return twitchRepository.getChannelVideos(userId, cursor)
    }
}