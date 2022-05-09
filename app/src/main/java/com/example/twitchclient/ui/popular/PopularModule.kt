package com.example.twitchclient.ui.popular

import androidx.lifecycle.ViewModel
import com.example.twitchclient.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PopularModule {

    @Binds
    @IntoMap
    @ViewModelKey(PopularViewModel::class)
    fun bindPopularViewModel(
        viewModel: PopularViewModel
    ): ViewModel
}