package com.example.twitchclient.ui.stream

import androidx.lifecycle.ViewModel
import com.example.twitchclient.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface StreamModule {

    @Binds
    @IntoMap
    @ViewModelKey(StreamViewModel::class)
    fun bindStreamViewModel(
        viewModel: StreamViewModel
    ): ViewModel
}