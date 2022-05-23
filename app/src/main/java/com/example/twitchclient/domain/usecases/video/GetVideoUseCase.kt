package com.example.twitchclient.domain.usecases.video

import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.repository.RoomRepository
import javax.inject.Inject

class GetVideoUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {

    suspend operator fun invoke(id: String): VideoInfo {
        return roomRepository.getVideo(id)
    }
}