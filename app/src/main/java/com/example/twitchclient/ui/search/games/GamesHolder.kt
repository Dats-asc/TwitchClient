package com.example.twitchclient.ui.search.games

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitchclient.databinding.ItemChannelBinding
import com.example.twitchclient.databinding.ItemGameBinding
import com.example.twitchclient.domain.entity.search.ChannelInfo
import com.example.twitchclient.domain.entity.search.GameInfo

class GamesHolder(
    private val binding: ItemGameBinding,
    private val parent: ViewGroup,
    private val action: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GameInfo) {
        with(binding) {
            Glide.with(itemView.context)
                .load(item.box_art_url)
                .into(gameIcon)
            tvGameTitle.text = item.name
        }
        itemView.setOnClickListener {
            action(item.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = GamesHolder(
            ItemGameBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent, action
        )
    }
}