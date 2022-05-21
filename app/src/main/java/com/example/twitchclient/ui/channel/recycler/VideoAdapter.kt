package com.example.twitchclient.ui.channel.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.domain.entity.videos.VideoInfo

class VideoAdapter(
    private val videos: ArrayList<VideoInfo>,
    private val onItemClicked: (String) -> Unit,
    private val onNextStreams: () -> Unit
) : RecyclerView.Adapter<VideoHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoHolder = VideoHolder.create(parent, onItemClicked)

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bind(videos[position])
        if (position < videos.size - 1 && position > videos.size - 3) {
            onNextStreams()
        }
    }

    fun updateData(newData: ArrayList<VideoInfo>) {
        val callback = VideoDiffUtils(videos, newData)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        videos.clear()
        videos.addAll(newData)
    }

    override fun getItemCount(): Int = videos.size
}