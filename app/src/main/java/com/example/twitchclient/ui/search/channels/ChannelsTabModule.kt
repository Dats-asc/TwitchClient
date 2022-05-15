package com.example.twitchclient.ui.search.channels

import androidx.lifecycle.ViewModel
import com.example.twitchclient.di.ViewModelKey
import com.example.twitchclient.ui.followings.FollowingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ChannelsTabModule {

    @Binds
    @IntoMap
    @ViewModelKey(FollowingsViewModel::class)
    fun bindChannelsTabViewModel(
        viewModel: ChannelsTabViewModel
    ): ViewModel
}