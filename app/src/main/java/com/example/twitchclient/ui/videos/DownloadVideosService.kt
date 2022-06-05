package com.example.twitchclient.ui.videos

import android.R
import android.app.Notification
import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Requirements.RequirementFlags
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.NotificationUtil
import com.google.android.exoplayer2.util.Util


class DownloadVideosService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.search_go,  /* channelDescriptionResourceId= */
    0
) {

    private lateinit var downloadManager: DownloadManager

    private lateinit var notificationHelper: DownloadNotificationHelper

    override fun getDownloadManager(): DownloadManager {
        notificationHelper = DownloadNotificationHelper(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
        downloadManager = DownloadUtils.getDownloadManager(this)
        return downloadManager
    }

    override fun getScheduler(): Scheduler? {
        return if (Util.SDK_INT >= 21) PlatformScheduler(this, JOB_ID) else null
    }

    override fun getForegroundNotification(
        downloads: List<Download>, notMetRequirements: @RequirementFlags Int
    ): Notification {
        return notificationHelper.buildProgressNotification(
            this,
            R.drawable.ic_menu_save,
            null,
            "Загрузка видео",
            downloads,
            notMetRequirements
        )
    }

    private class TerminalStateNotificationHelper(
        context: Context, notificationHelper: DownloadNotificationHelper, firstNotificationId: Int
    ) :
        DownloadManager.Listener {
        private val context: Context
        private val notificationHelper: DownloadNotificationHelper
        private var nextNotificationId: Int
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            val notification: Notification =
                when (download.state) {
                    Download.STATE_COMPLETED -> {
                        notificationHelper.buildDownloadCompletedNotification(
                            context,
                            R.drawable.ic_menu_upload,  /* contentIntent= */
                            null,
                            Util.fromUtf8Bytes(download.request.data)
                        )
                    }
                    Download.STATE_FAILED -> {
                        notificationHelper.buildDownloadFailedNotification(
                            context,
                            R.drawable.ic_menu_save,  /* contentIntent= */
                            null,
                            Util.fromUtf8Bytes(download.request.data)
                        )
                    }
                    else -> {
                        return
                    }
                }
            NotificationUtil.setNotification(context, nextNotificationId++, notification)
        }

        init {
            this.context = context.applicationContext
            this.notificationHelper = notificationHelper
            nextNotificationId = firstNotificationId
        }
    }

    companion object {
        private const val JOB_ID = 1
        private const val FOREGROUND_NOTIFICATION_ID = 1
        private const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"
    }
}