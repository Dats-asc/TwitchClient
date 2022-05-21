package com.example.twitchclient.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitchclient.domain.entity.user.Token
import com.example.twitchclient.domain.usecases.twitch.PutAccessTokenUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val putAccessTokenUseCase: PutAccessTokenUseCase
) : ViewModel() {

    fun putAccessToken(token: String) {
        viewModelScope.launch {
            putAccessTokenUseCase(Token(token))
        }
    }
}