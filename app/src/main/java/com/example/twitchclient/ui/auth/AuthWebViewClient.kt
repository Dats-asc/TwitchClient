package com.example.twitchclient.ui.auth

import android.webkit.WebView
import android.webkit.WebViewClient

internal class AuthWebViewClient : WebViewClient() {

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {

        super.doUpdateVisitedHistory(view, url, isReload)
    }
}