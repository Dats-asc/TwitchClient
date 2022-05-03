package com.example.twitchclient.ui.chat

import android.text.style.ImageSpan

data class MessageImageSpan(
    val name: String,
    val starts: Int,
    val ends: Int,
    val span: ImageSpan
)