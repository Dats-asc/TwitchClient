package com.example.twitchclient.ui.stream

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.StreamFragmentBinding
import com.example.twitchclient.ui.chat.ChatFragment
import com.example.twitchclient.ui.chat.ChatViewModel
import com.example.twitchclient.ui.chat.LiveChatService
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.navigation.navigator
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StreamFragment : Fragment() {

    private lateinit var binding: StreamFragmentBinding

    private lateinit var broadcasterLogin: String

    private val viewModel: StreamViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = StreamFragmentBinding.inflate(inflater, container, false).let {
        binding = StreamFragmentBinding.inflate(inflater, container, false)
        arguments?.let { bundle ->
            broadcasterLogin = bundle.getString(C.BROADCASTER_LOGIN) ?: ""
        }
        initChatContainer()
        startStream()
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }

    private fun initChatContainer() {
        with(binding) {
            childFragmentManager.beginTransaction().run {
                add(R.id.chat_container, ChatFragment().apply {
                    arguments = bundleOf(C.BROADCASTER_LOGIN to broadcasterLogin)
                })
                commit()
            }
        }
    }

    private fun setPlayer(player: ExoPlayer) {
        binding.streamPlayerView.player = player
        viewModel.setQualityOption(3)
    }

    private fun startStream() {
        viewModel.start(
            onServiceBind = { broadcasterLogin },
            onPlayerCreated = { player ->
                setPlayer(player)
            }
        )
    }

}