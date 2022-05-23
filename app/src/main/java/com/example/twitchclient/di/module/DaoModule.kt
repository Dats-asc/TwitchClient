package com.example.twitchclient.di.module

import com.example.twitchclient.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {

    @Provides
    @Singleton
    fun provideVideoDao(
        appDatabase: AppDatabase
    ) = appDatabase.videoDao()
}