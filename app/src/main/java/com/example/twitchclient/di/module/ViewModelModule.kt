package com.example.twitchclient.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twitchclient.di.ViewModelKey
import com.example.twitchclient.ui.followings.FollowingsViewModel
import com.example.twitchclient.ui.main.MainViewModel
import com.example.twitchclient.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(
        viewModel: MainViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FollowingsViewModel::class)
    fun bindFollowingsViewModel(
        viewModel: FollowingsViewModel
    ): ViewModel
}