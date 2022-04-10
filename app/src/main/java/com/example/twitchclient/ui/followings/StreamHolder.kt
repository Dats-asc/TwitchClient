package com.example.twitchclient.ui.followings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitchclient.databinding.ItemStreamBinding
import com.example.twitchclient.domain.entity.streams.StreamData

class StreamHolder (
    private val binding: ItemStreamBinding,
    private val action: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root){

    fun bind(item: StreamData){
        with(binding){
            Glide.with(itemView.context)
                .load(item.thumbnail_url)
                .into(ivStreamPreview)
            tvChannelName.text = item.user_name
            tvStreamTitle.text = item.title
            tvStreamCategory.text = item.game_name
        }
        itemView.setOnClickListener{
            action(item.user_login)
        }
    }

    companion object{
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = StreamHolder(
            ItemStreamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), action
        )
    }
}