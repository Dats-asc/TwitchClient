package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.data.responses.twitch.user.Data
import com.example.twitchclient.data.responses.twitch.user.UserResponse

class PingUserUseCase(
    val twitchRepository: TwitchRepositoryImpl
) {

    suspend operator fun invoke(): UserResponse {
        return twitchRepository.pingUser()
    }
}