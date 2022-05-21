package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.user.UserDetail
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    suspend operator fun invoke(userId: String): UserDetail {
        return twitchRepository.getUserDetail(userId)
    }
}