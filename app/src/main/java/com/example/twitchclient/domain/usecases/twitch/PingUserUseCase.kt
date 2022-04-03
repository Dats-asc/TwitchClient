package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.domain.entity.user.User

class PingUserUseCase(
    val twitchRepository: TwitchRepositoryImpl
) {

    suspend operator fun invoke(): User {
        return twitchRepository.pingUser()
    }
}