package com.example.twitchclient.ui.search

import androidx.lifecycle.ViewModel
import com.example.twitchclient.di.ViewModelKey
import com.example.twitchclient.ui.followings.FollowingsViewModel
import com.example.twitchclient.ui.search.channels.ChannelsTabViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun bindSearchViewModel(
        viewModel: SearchViewModel
    ): ViewModel
}