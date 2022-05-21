package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.entity.user.Token
import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class PutAccessTokenUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    operator fun invoke(token: Token) {
        twitchRepository.putAccessToken(token)
    }
}