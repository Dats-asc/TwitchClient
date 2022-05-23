package com.example.twitchclient.domain.usecases.video

import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.repository.RoomRepository
import javax.inject.Inject

class DeleteVideoUseCase @Inject constructor(
    private val roomRepository: RoomRepository
) {

    suspend operator fun invoke(id: String) = roomRepository.deleteVideo(id)
}