package com.example.twitchclient.domain.entity.user

data class User(
    val id: String,
    val login: String,
    val display_name: String,
    val description: String,
    val profile_image_url: String,
    val view_count: Int,
    val created_at: String,
)