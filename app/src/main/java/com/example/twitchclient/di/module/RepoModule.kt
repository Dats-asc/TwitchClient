package com.example.twitchclient.di.module

import com.example.twitchclient.data.repository.BttvFfzRepositoryImpl
import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.data.repository.UsherRepositoryImpl
import com.example.twitchclient.data.room.repository.RoomRepositoryImpl
import com.example.twitchclient.domain.repository.BttvFfzRepository
import com.example.twitchclient.domain.repository.RoomRepository
import com.example.twitchclient.domain.repository.TwitchRepository
import com.example.twitchclient.domain.repository.UsherRepository
import dagger.Binds
import dagger.Module

@Module
interface RepoModule {

    @Binds
    fun twitchRepository(
        impl: TwitchRepositoryImpl
    ): TwitchRepository

    @Binds
    fun bttvFfzRepository(
        impl: BttvFfzRepositoryImpl
    ): BttvFfzRepository

    @Binds
    fun roomRepository(
        impl: RoomRepositoryImpl
    ): RoomRepository

    @Binds
    fun usherRepository(
        impl: UsherRepositoryImpl
    ): UsherRepository
}