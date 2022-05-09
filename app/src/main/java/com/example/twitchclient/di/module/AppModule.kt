package com.example.twitchclient.di.module

import android.content.Context
import com.example.twitchclient.MyApp
import com.example.twitchclient.data.api.mapper.BttvFfzMapper
import com.example.twitchclient.data.api.mapper.TwitchMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(app: MyApp): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideAccessToken(
        context: Context
    ): String? =
        context.getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE)
            .getString("USER_ACCESS_TOKEN_VALUE", "")

    @Provides
    @Singleton
    fun provideTwitchMapper(): TwitchMapper = TwitchMapper()

    @Provides
    @Singleton
    fun provideBttvFfzMapper(): BttvFfzMapper = BttvFfzMapper()
}