package com.example.twitchclient.ui.auth

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.twitchclient.R
import com.example.twitchclient.databinding.AuthFragmentBinding
import com.example.twitchclient.ui.MainActivity

class AuthFragment : Fragment() {

    companion object {
        fun newInstance() = AuthFragment()

        private val AUTH_URL =
            "https://id.twitch.tv/oauth2/authorize?client_id=sovhd4glbv1x233g3peqkst60l7e2p&redirect_uri=https://localhost&response_type=token&scope=analytics:read:extensions%20analytics:read:games%20bits:read%20channel:edit:commercial%20channel:manage:broadcast%20channel:manage:extensions%20channel:manage:polls%20channel:manage:predictions%20channel:manage:redemptions%20channel:manage:schedule%20channel:manage:videos%20channel:read:editors%20channel:read:goals%20channel:read:hype_train%20channel:read:polls%20channel:read:predictions%20channel:read:redemptions%20channel:read:stream_key%20channel:read:subscriptions%20clips:edit%20moderation:read%20user:edit%20user:edit:follows%20user:manage:blocked_users%20user:read:blocked_users%20user:read:broadcast%20user:read:email%20user:read:follows%20user:read:subscriptions%20channel:moderate%20chat:edit%20chat:read"
        private val CALLBACK_URL = "https://localhost"

        private val TOKEN_EXAMPLE = "https://localhost/#access_token=gsnsg8g0qjhr6d4wvniiu0dfvrxxqv&scope=analytics%3Aread%3Aextensions+analytics%3Aread%3Agames+bits%3Aread+channel%3Aedit%3Acommercial+channel%3Amanage%3Abroadcast+channel%3Amanage%3Aextensions+channel%3Amanage%3Apolls+channel%3Amanage%3Apredictions+channel%3Amanage%3Aredemptions+channel%3Amanage%3Aschedule+channel%3Amanage%3Avideos+channel%3Aread%3Aeditors+channel%3Aread%3Agoals+channel%3Aread%3Ahype_train+channel%3Aread%3Apolls+channel%3Aread%3Apredictions+channel%3Aread%3Aredemptions+channel%3Aread%3Astream_key+channel%3Aread%3Asubscriptions+clips%3Aedit+moderation%3Aread+user%3Aedit+user%3Aedit%3Afollows+user%3Amanage%3Ablocked_users+user%3Aread%3Ablocked_users+user%3Aread%3Abroadcast+user%3Aread%3Aemail+user%3Aread%3Afollows+user%3Aread%3Asubscriptions+channel%3Amoderate+chat%3Aedit+chat%3Aread&token_type=bearer"
    }

    private lateinit var binding: AuthFragmentBinding

    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AuthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        initWebView()

        saveAccessToken(TOKEN_EXAMPLE)
    }

    private fun initWebView(){
        with(binding.webview){
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Handler(Looper.getMainLooper()).post {
                        saveAccessToken(view?.url ?: "")
                    }
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }
            //loadUrl(AUTH_URL)
            loadUrl("https://yandex.ru")
        }
    }

    private fun saveAccessToken(redirectUrl: String){
        val token = getToken(redirectUrl)
        (activity as MainActivity).putAccessToken(token)
    }

    private fun getToken(url: String) =
        url.split('/')[3].split('&')[0].split("#access_token=")[1]

}