package com.example.twitchclient.di.module

import android.content.Context
import com.example.twitchclient.data.api.mapper.BttvFfzMapper
import com.example.twitchclient.data.api.mapper.TwitchMapper
import dagger.Module
import dagger.Provides


@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @Provides
    fun provideAccessToken(
        context: Context
    ): String? =
        context.getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE).getString("USER_ACCESS_TOKEN_VALUE", "")

    @Provides
    fun provideTwitchMapper(): TwitchMapper = TwitchMapper()

    @Provides
    fun provideBttvFfzMapper() : BttvFfzMapper = BttvFfzMapper()
}