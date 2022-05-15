package com.example.twitchclient.ui.search.channels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitchclient.databinding.ItemChannelBinding
import com.example.twitchclient.domain.entity.search.ChannelInfo

class ChannelsHolder(
    private val binding: ItemChannelBinding,
    private val parent: ViewGroup,
    private val action: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChannelInfo) {
        with(binding) {
            Glide.with(itemView.context)
                .load(item.thumbnail_url)
                .into(channelIcon)
            tvChannelName.text = item.display_name
        }
        itemView.setOnClickListener {
            action(item.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = ChannelsHolder(
            ItemChannelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent, action
        )
    }
}