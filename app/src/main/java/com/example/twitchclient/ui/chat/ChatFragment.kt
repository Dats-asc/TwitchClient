package com.example.twitchclient.ui.chat

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ChatFragmentBinding
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.domain.entity.chat.ChatMessage
import com.example.twitchclient.ui.followings.FollowingsViewModel
import com.example.twitchclient.ui.main.MainActivity
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatFragment : Fragment() {

    private lateinit var binding: ChatFragmentBinding

    private lateinit var chatAdapter: ChatAdapter

    private var chatMessages = mutableListOf<ChatMessage>()

    private val viewModel: ChatViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ChatFragmentBinding.inflate(inflater, container, false)?.let {
        binding = ChatFragmentBinding.inflate(inflater, container, false)
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.startChat()
        initObservers()

        binding.btnSend.setOnClickListener {
            viewModel.sendMessage(binding.tiSendMessage.text.toString())
            val a = 0
        }
    }

    private fun initObservers() {
        viewModel.queryChatMessages.observe(activity as MainActivity) {
            it.fold(
                onSuccess = { chatMessage ->
                    chatMessages.add(chatMessage)
//                    chatAdapter = ChatAdapter(chatMessages, {
//                        //TODO
//                    })
                    chatAdapter.notifyItemInserted(chatAdapter.itemCount)
                    binding.rvLiveChat.scrollToPosition(chatAdapter.itemCount - 1)
                },
                onFailure = {

                }
            )
        }
    }

    private fun initAdapter(){
        chatAdapter = ChatAdapter(chatMessages, {})
        binding.rvLiveChat.adapter = chatAdapter
    }

}