package com.example.twitchclient.ui.start

import androidx.lifecycle.ViewModel
import com.example.twitchclient.domain.usecases.twitch.GetAccessTokenUseCase
import javax.inject.Inject

class StartViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    val isAuthorized get() = getAccessTokenUseCase().isNotEmpty()
}