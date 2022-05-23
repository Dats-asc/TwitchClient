package com.example.twitchclient.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.twitchclient.data.room.entity.VideoEntity

@Database(
    entities = [
        VideoEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao
}