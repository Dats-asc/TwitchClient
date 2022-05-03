package com.example.twitchclient.ui.chat

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.getSpans
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.example.twitchclient.databinding.ItemChatMessageBinding
import com.example.twitchclient.domain.entity.emotes.bttv.BttvChanelEmotes
import com.example.twitchclient.domain.entity.emotes.twitch.TwitchGlobalEmotes

class ChatHolder(
    private val binding: ItemChatMessageBinding,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        spannableString: SpannableStringBuilder
    ) {
        binding.tvMessage.text = spannableString
    }

    private fun loadImage() {

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