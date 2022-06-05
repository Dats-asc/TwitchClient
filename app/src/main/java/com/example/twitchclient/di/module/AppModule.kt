package com.example.twitchclient.di.module

import android.content.Context
import android.content.SharedPreferences
import com.example.twitchclient.C
import com.example.twitchclient.MyApp
import com.example.twitchclient.data.api.mapper.BttvFfzMapper
import com.example.twitchclient.data.api.mapper.TwitchMapper
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
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
    fun provideSharedPreferences(app: MyApp): SharedPreferences {
        return with(app.applicationContext) {
            getSharedPreferences(C.USER_PREFERENCES, Context.MODE_PRIVATE)
        }
    }

    @Provides
    @Singleton
    fun provideAccessToken(
        app: MyApp
    ): String =
        app.applicationContext.getSharedPreferences(C.USER_PREFERENCES, Context.MODE_PRIVATE)
            .getString(C.USER_ACCESS_TOKEN_VALUE, "").orEmpty()

    @Provides
    @Singleton
    fun provideTwitchMapper(): TwitchMapper = TwitchMapper()

    @Provides
    @Singleton
    fun provideBttvFfzMapper(): BttvFfzMapper = BttvFfzMapper()

    @Provides
    @Singleton
    fun provideStandaloneDatabaseProvider(context: Context): StandaloneDatabaseProvider = StandaloneDatabaseProvider(context)

//    @Provides
//    @Singleton
//    fun provideSimpleCache(
//        context: Context,
//        standaloneDatabaseProvider: StandaloneDatabaseProvider
//    ) = SimpleCache(
//        context.cacheDir,
//        NoOpCacheEvictor(),
//        standaloneDatabaseProvider
//    )

    @Provides
    @Singleton
    fun provideDownloadManager(
        context: Context
    ): DownloadManager {
        val dbProvider = StandaloneDatabaseProvider(context)
        val downloadCache = SimpleCache(context.cacheDir, NoOpCacheEvictor(), dbProvider)

        return DownloadManager(
            context,
            dbProvider,
            downloadCache,
            DefaultHttpDataSource.Factory(),
            Runnable::run
        )
    }
}