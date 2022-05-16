package com.example.twitchclient.ui.games

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.twitchclient.data.api.mapper.TwitchMapper
import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.databinding.GamesFragmentBinding
import com.example.twitchclient.domain.usecases.twitch.PingUserUseCase
import com.example.twitchclient.ui.chat.ChatFragment
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.navigation.navigator
import kotlinx.coroutines.launch

class GamesFragment : Fragment() {

    private lateinit var binding: GamesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = GamesFragmentBinding.inflate(inflater, container, false).let {
        binding = it
        binding.toolbar.setupWithNavController(findNavController())
        binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}