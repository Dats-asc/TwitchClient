package com.example.twitchclient.ui.channel.recycler

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ItemVideoBinding
import com.example.twitchclient.domain.entity.videos.VideoInfo

class VideoHolder(
    private val binding: ItemVideoBinding,
    private val parent: ViewGroup,
    private val action: (VideoInfo) -> Unit,
    private val onItemMenuClicked: (VideoInfo, v: View) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: VideoInfo) {
        with(binding) {
            Glide.with(itemView.context)
                .load(item.previewUrl)
                .into(videoPreview)
            tvVideoDescription.text = item.title
            tvPublishingDate.text = "Опубликованно: ${item.publishedAt.split("T")[0]}"
            tvVideoDuration.text = item.duration
            tvViewersCount.text = "Просмотров: ${item.viewCount}"
        }
        itemView.setOnClickListener {
            action(item)
        }
        binding.ivMenu.setOnClickListener {
            onItemMenuClicked(item, it)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (VideoInfo) -> Unit,
            onItemMenuClicked: (VideoInfo, View) -> Unit
        ) = VideoHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent, action, onItemMenuClicked
        )
    }
}