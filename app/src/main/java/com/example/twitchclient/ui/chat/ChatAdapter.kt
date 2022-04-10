package com.example.twitchclient.ui.chat

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.domain.entity.chat.ChatMessage

class ChatAdapter (
    private val songs: List<ChatMessage>,
    private val action: (Int) -> Unit
) : RecyclerView.Adapter<ChatHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatHolder = ChatHolder.create(parent, action)

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount(): Int = songs.size
}