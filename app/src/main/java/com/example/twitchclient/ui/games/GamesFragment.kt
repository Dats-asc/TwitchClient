package com.example.twitchclient.ui.games

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.example.twitchclient.Constants
import com.example.twitchclient.R
import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.databinding.GamesFragmentBinding
import com.example.twitchclient.domain.usecases.twitch.PingUserUseCase
import com.example.twitchclient.ui.MainActivity
import kotlinx.coroutines.launch

class GamesFragment : Fragment() {

    companion object {
        fun newInstance() = GamesFragment()
    }

    private lateinit var binding: GamesFragmentBinding

    private lateinit var viewModel: GamesViewModel

    private lateinit var pingUserUseCase: PingUserUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GamesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GamesViewModel::class.java)

        pingUserUseCase = PingUserUseCase(TwitchRepositoryImpl((activity as MainActivity).getAccessToken() ?: ""))

        binding.token.text = (activity as MainActivity).getAccessToken() ?: "not found"

        getUser()

    }

    private fun getUser(){
        lifecycleScope.launch {
            var user = pingUserUseCase.invoke()

            binding.userInfo.text = user.data[0].login + "  " + user.data[0].id
        }
    }

}