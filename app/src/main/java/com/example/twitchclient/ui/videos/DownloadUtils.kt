package com.example.twitchclient.ui.videos

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache

object DownloadUtils {

    private var videoCache: SimpleCache? = null

    private var dbProvider: StandaloneDatabaseProvider? = null

    private var downloadManager: DownloadManager? = null

    fun getVideoCache(context: Context): SimpleCache {
        if (videoCache == null) {
            dbProvider = if (dbProvider == null) StandaloneDatabaseProvider(context) else dbProvider
            videoCache = SimpleCache(
                context.cacheDir,
                NoOpCacheEvictor(),
                dbProvider!!
            )
        }
        return videoCache!!
    }

    fun getDownloadManager(context: Context): DownloadManager {
        if (downloadManager == null) {
            if (videoCache == null) {
                dbProvider =
                    if (dbProvider == null) StandaloneDatabaseProvider(context) else dbProvider
                videoCache = SimpleCache(
                    context.cacheDir,
                    NoOpCacheEvictor(),
                    dbProvider!!
                )
            }

            downloadManager = DownloadManager(
                context,
                dbProvider!!,
                videoCache!!,
                DefaultHttpDataSource.Factory(),
                Runnable::run
            )
        }

        return downloadManager!!
    }
}