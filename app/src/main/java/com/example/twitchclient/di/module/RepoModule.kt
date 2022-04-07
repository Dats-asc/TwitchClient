package com.example.twitchclient.di.module

import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.domain.repository.TwitchRepository
import dagger.Binds
import dagger.Module

@Module
interface RepoModule {

    @Binds
    fun twitchRepository(
        impl: TwitchRepositoryImpl
    ) : TwitchRepository
}