package com.example.twitchclient.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.databinding.ItemChatMessageBinding
import com.example.twitchclient.domain.entity.chat.ChatMessage

class ChatHolder(
    private val binding: ItemChatMessageBinding,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChatMessage) {
        with(binding) {
            tvUsername.text = "${item.username} : "
            tvMessage.text = item.message
        }
        itemView.setOnClickListener {
            action(item.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (Int) -> Unit
        ) = ChatHolder(
            ItemChatMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), action
        )
    }
}