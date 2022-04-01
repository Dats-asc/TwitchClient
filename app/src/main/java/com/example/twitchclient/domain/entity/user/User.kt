package com.example.twitchclient.domain.entity.user

class User(
    val broadcaster_type: String,
    val created_at: String,
    val description: String,
    val display_name: String,
    val email: String,
    val id: String,
    val login: String,
    val offline_image_url: String,
    val profile_image_url: String,
    val type: String,
    val view_count: Int
)