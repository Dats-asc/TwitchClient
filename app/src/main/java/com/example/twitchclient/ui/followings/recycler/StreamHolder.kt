package com.example.twitchclient.ui.followings.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.twitchclient.databinding.ItemStreamBinding
import com.example.twitchclient.domain.entity.streams.StreamData

class StreamHolder (
    private val binding: ItemStreamBinding,
    private val parent: ViewGroup,
    private val action: (StreamData) -> Unit,
    private val onChannelAvatarClicked: (StreamData) -> Unit
) : RecyclerView.ViewHolder(binding.root){

    fun bind(item: StreamData){
        with(binding){
            Glide.with(itemView.context)
                .load(item.thumbnail_url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivStreamPreview)
            Glide.with(itemView.context)
                .load(item.profile_image)
                .into(ivStreamerAvatar)
            tvChannelName.text = item.user_name
            tvStreamTitle.text = item.title
            tvStreamCategory.text = item.game_name
            tvViewersCount.text = item.viewer_count.toString()
        }
        itemView.setOnClickListener{
            action(item)
        }
        binding.ivStreamerAvatar.setOnClickListener {
            onChannelAvatarClicked(item)
        }
    }

    companion object{
        fun create(
            parent: ViewGroup,
            action: (StreamData) -> Unit,
            onChannelAvatarClicked: (StreamData) -> Unit
        ) = StreamHolder(
            ItemStreamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent ,action, onChannelAvatarClicked
        )
    }
}