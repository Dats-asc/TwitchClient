package com.example.twitchclient.domain.usecases.video

import com.example.twitchclient.data.room.repository.RoomRepositoryImpl
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.repository.RoomRepository
import javax.inject.Inject

class AddVideoUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {

    suspend operator fun invoke(video: VideoInfo) {
        roomRepository.addVideo(video)
    }
}