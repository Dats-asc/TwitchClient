package com.example.twitchclient.di.module

import android.content.Context
import com.example.twitchclient.data.api.mapper.TwitchMapper
import dagger.Module
import dagger.Provides


@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @Provides
    fun provideTwitchMapper(): TwitchMapper = TwitchMapper()
}