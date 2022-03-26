package com.example.twitchclient.ui.auth

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.example.twitchclient.databinding.ActivityAuthorizationBinding
import java.net.URI
import java.net.URL


class AuthorizationActivity : AppCompatActivity() {

    companion object {
        private val AUTH_URL =
            "https://id.twitch.tv/oauth2/authorize?client_id=sovhd4glbv1x233g3peqkst60l7e2p&redirect_uri=https://localhost:5001&response_type=token&scope=analytics:read:extensions%20analytics:read:games%20bits:read%20channel:edit:commercial%20channel:manage:broadcast%20channel:manage:extensions%20channel:manage:polls%20channel:manage:predictions%20channel:manage:redemptions%20channel:manage:schedule%20channel:manage:videos%20channel:read:editors%20channel:read:goals%20channel:read:hype_train%20channel:read:polls%20channel:read:predictions%20channel:read:redemptions%20channel:read:stream_key%20channel:read:subscriptions%20clips:edit%20moderation:read%20user:edit%20user:edit:follows%20user:manage:blocked_users%20user:read:blocked_users%20user:read:broadcast%20user:read:email%20user:read:follows%20user:read:subscriptions%20channel:moderate%20chat:edit%20chat:read"

        private val CALLBACK_URL = "https://localhost"
    }

    private var REDIRECT_URL = ""
    private lateinit var binding: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWebView(AUTH_URL)

        val a = REDIRECT_URL
    }

    private fun setupWebView(url: String) {
        binding.wvAuth.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            webViewClient = object : WebViewClient() {

                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    Handler(Looper.getMainLooper()).post {
                        setUserAccessToken(view?.url ?: "")
                        REDIRECT_URL = view?.url ?: "wrong url"
                    }
                    return super.shouldInterceptRequest(view, request)
                }
            }
            loadUrl(url)
        }
    }

    private fun setUserAccessToken(redirectUrl: String) {
        val a = 0
    }

}