package com.example.twitchclient.ui.followings

import androidx.lifecycle.ViewModel
import com.example.twitchclient.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FollowingsModule {

    @Binds
    @IntoMap
    @ViewModelKey(FollowingsViewModel::class)
    fun bindFollowingsViewModel(
        viewModel: FollowingsViewModel
    ): ViewModel
}