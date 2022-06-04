package com.example.twitchclient.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat")
data class ChatEntity(
    @PrimaryKey val id: Int = 0,
    val users: ArrayList<ChatUser>
)

@Entity
data class ChatUser(
    @PrimaryKey val login: String
)