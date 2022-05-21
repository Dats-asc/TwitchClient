package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(id: String): User {
        return twitchRepository.getUserById(id)
    }
}