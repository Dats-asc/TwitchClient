package com.example.twitchclient.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.twitchclient.C
import com.example.twitchclient.databinding.ChatFragmentBinding
import com.example.twitchclient.domain.entity.chat.ChatMessage
import com.example.twitchclient.domain.entity.emotes.ChatEmotes
import com.example.twitchclient.domain.entity.emotes.GeneralEmotes
import com.example.twitchclient.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class ChatFragment : Fragment() {

    private lateinit var binding: ChatFragmentBinding

    private lateinit var chatAdapter: ChatAdapter

    private var chatMessages = mutableListOf<ChatMessage>()

    private var isScrollStopped = false

    private lateinit var broadcasterLogin: String

    private lateinit var chatEmotes: ChatEmotes

    private val viewModel: ChatViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ChatFragmentBinding.inflate(inflater, container, false)?.let {
        binding = ChatFragmentBinding.inflate(inflater, container, false)
        arguments?.let { bundle ->
            broadcasterLogin = bundle.getString(C.BROADCASTER_LOGIN) ?: ""
        }
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startChat(broadcasterLogin) {
            onChatLoaded()
        }
        initObservers()
        binding.btnSendMessage.setOnClickListener {
            viewModel.sendMessage(binding.tiMessage.text.toString())
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopChat()
    }

    private fun onChatLoaded() {
        Snackbar.make(
            binding.root,
            "Загружен чат пользователя $broadcasterLogin",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun initObservers() {
        viewModel.queryChatMessages.observe(viewLifecycleOwner) {
            it.fold(
                onSuccess = { chatMessage ->
                    onChatMessage(chatMessage)
                },
                onFailure = { Log.e("On chat message", it.message.toString()) }
            )
        }
        viewModel.allEmotes.observe(viewLifecycleOwner) {
            it.fold(
                onSuccess = { chatEmotes ->
                    onChatDataLoaded(chatEmotes)
                },
                onFailure = {
                    Log.e("Get emote fail", it.message.toString())
                }
            )
        }
    }

    private fun onChatDataLoaded(chatEmotes: GeneralEmotes) {
        initAdapter(chatEmotes)
    }

    private fun onChatMessage(chatMessage: ChatMessage) {
        chatMessages.add(chatMessage)
        chatAdapter.notifyItemInserted(chatAdapter.itemCount - 1)
        if (!isScrollStopped) {
            binding.rvLiveChat.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    private fun initAdapter(chatEmotes: GeneralEmotes) {
        chatAdapter = ChatAdapter(
            this,
            chatMessages,
            chatEmotes,
            {}
        )
        binding.rvLiveChat.adapter = chatAdapter
        binding.rvLiveChat.itemAnimator = null
    }

}