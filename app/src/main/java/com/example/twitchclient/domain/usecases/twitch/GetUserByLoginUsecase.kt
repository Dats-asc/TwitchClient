package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.user.User
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetUserByLoginUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(login: String): User {
        return twitchRepository.getUserByLogin(login)
    }
}