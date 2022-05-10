package com.example.twitchclient.ui.games

import androidx.lifecycle.ViewModel
import com.example.twitchclient.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface GamesModule {

    @Binds
    @IntoMap
    @ViewModelKey(GamesViewModel::class)
    fun bindGamesViewModel(
        viewModel: GamesViewModel
    ): ViewModel
}