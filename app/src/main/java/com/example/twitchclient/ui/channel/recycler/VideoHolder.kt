package com.example.twitchclient.ui.channel.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twitchclient.databinding.ItemVideoBinding
import com.example.twitchclient.domain.entity.videos.VideoInfo

class VideoHolder(
    private val binding: ItemVideoBinding,
    private val parent: ViewGroup,
    private val action: (String) -> Unit
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
            action(item.id)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = VideoHolder(
            ItemVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), parent, action
        )
    }
}