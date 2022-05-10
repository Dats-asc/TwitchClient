package com.example.twitchclient.ui.chat

import androidx.lifecycle.ViewModel
import com.example.twitchclient.di.ViewModelKey
import com.example.twitchclient.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ChatModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    fun bindChatViewModel(
        viewModel: ChatViewModel
    ): ViewModel
}