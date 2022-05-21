package com.example.twitchclient.domain.entity.user

data class UserDetail(
    val broadcaster_type: String,
    val created_at: String,
    val description: String,
    val display_name: String,
    val id: String,
    val login: String,
    val offline_image_url: String,
    val profile_image_url: String,
    val view_count: Int,
    val viewer_count: Int,
    val started_at: String,
    val is_live: Boolean,
)