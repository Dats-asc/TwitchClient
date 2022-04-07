package com.example.twitchclient.di.module

import com.example.twitchclient.data.api.mapper.TwitchMapper
import dagger.Module
import dagger.Provides


@Module
class AppModule {

    @Provides
    fun provideTwitchMapper(): TwitchMapper = TwitchMapper()
}