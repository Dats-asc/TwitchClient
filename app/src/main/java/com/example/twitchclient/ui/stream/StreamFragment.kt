package com.example.twitchclient.ui.stream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.StreamFragmentBinding
import com.example.twitchclient.ui.chat.ChatFragment
import com.example.twitchclient.ui.navigation.navigator
import com.google.android.exoplayer2.ui.PlayerView

class StreamFragment : Fragment() {

    private lateinit var binding: StreamFragmentBinding

    private lateinit var viewModel: StreamViewModel

    private lateinit var broadcasterLogin: String

    private var streamBundle: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = StreamFragmentBinding.inflate(inflater, container, false).let {
        binding = StreamFragmentBinding.inflate(inflater, container, false)
        arguments?.let { bundle ->
            broadcasterLogin = bundle.getString(C.BROADCASTER_LOGIN) ?: ""
        }
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChatContainer()
    }

    private fun initChatContainer(){
        with(binding){
            childFragmentManager.beginTransaction().run {
                add(R.id.chat_container, ChatFragment().apply {
                    arguments = bundleOf(C.BROADCASTER_LOGIN to broadcasterLogin)
                })
                commit()
            }
        }
    }

}