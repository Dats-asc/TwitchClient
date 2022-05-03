package com.example.twitchclient

object C {
    const val CLIENT_ID = "sovhd4glbv1x233g3peqkst60l7e2p"
    const val AUTH_URL = "https://id.twitch.tv/oauth2/authorize?client_id=sovhd4glbv1x233g3peqkst60l7e2p&redirect_uri=https://localhost&response_type=token&scope=analytics:read:extensions%20analytics:read:games%20bits:read%20channel:edit:commercial%20channel:manage:broadcast%20channel:manage:extensions%20channel:manage:polls%20channel:manage:predictions%20channel:manage:redemptions%20channel:manage:schedule%20channel:manage:videos%20channel:read:editors%20channel:read:goals%20channel:read:hype_train%20channel:read:polls%20channel:read:predictions%20channel:read:redemptions%20channel:read:stream_key%20channel:read:subscriptions%20clips:edit%20moderation:read%20user:edit%20user:edit:follows%20user:manage:blocked_users%20user:read:blocked_users%20user:read:broadcast%20user:read:email%20user:read:follows%20user:read:subscriptions%20channel:moderate%20chat:edit%20chat:read"
    const val TWITCH_CHAT_WSS_SERVER = "wss://irc-ws.chat.twitch.tv:443"
    const val BROADCASTER_LOGIN = "BROADCASTER_LOGIN"
    const val BROADCASTER_ID = "BROADCASTER_ID"
}