package com.example.twitchclient.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.twitchclient.C
import com.example.twitchclient.data.room.AppDatabase
import com.example.twitchclient.data.room.DatabaseMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DaoModule::class])
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            C.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseMapper() = DatabaseMapper()
}