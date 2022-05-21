package com.example.twitchclient.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twitchclient.di.ViewModelKey
import com.example.twitchclient.ui.auth.AuthViewModel
import com.example.twitchclient.ui.channel.ChannelDetailViewModel
import com.example.twitchclient.ui.chat.ChatViewModel
import com.example.twitchclient.ui.followings.FollowingsViewModel
import com.example.twitchclient.ui.game.GameViewModel
import com.example.twitchclient.ui.games.GamesViewModel
import com.example.twitchclient.ui.popular.PopularViewModel
import com.example.twitchclient.ui.search.SearchViewModel
import com.example.twitchclient.ui.search.channels.ChannelsTabViewModel
import com.example.twitchclient.ui.search.games.GamesTabViewModel
import com.example.twitchclient.ui.start.StartViewModel
import com.example.twitchclient.ui.stream.StreamViewModel
import com.example.twitchclient.utils.MyViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(
        factoryMy: MyViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun bindAuthViewModel(
        viewModel: AuthViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowingsViewModel::class)
    fun bindFollowingsViewModel(
        viewModel: FollowingsViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    fun bindChatViewModel(
        viewModel: ChatViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StreamViewModel::class)
    fun bindStreamViewModel(
        viewModel: StreamViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChannelsTabViewModel::class)
    fun bindChannelsTabViewModel(
        viewModel: ChannelsTabViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(
        viewModel: SearchViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GamesTabViewModel::class)
    fun bindGamesTabViewModel(
        viewModel: GamesTabViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PopularViewModel::class)
    fun bindPopularViewModel(
        viewModel: PopularViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GamesViewModel::class)
    fun bindGamesViewModel(
        viewModel: GamesViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    fun bindGameViewModel(
        viewModel: GameViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChannelDetailViewModel::class)
    fun bindChannelDetailViewModel(
        viewModel: ChannelDetailViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartViewModel::class)
    fun bindStartViewModel(
        viewModel: StartViewModel
    ): ViewModel
}