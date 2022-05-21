package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class DeleteAccessTokenUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    operator fun invoke() = twitchRepository.deleteToken()
}