package com.example.twitchclient.domain.usecases.twitch

import com.example.twitchclient.domain.repository.TwitchRepository
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val twitchRepository: TwitchRepository
) {

    operator fun invoke() = twitchRepository.getAccessToken().accessToken
}