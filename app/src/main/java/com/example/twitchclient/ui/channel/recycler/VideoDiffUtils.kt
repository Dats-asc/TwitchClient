package com.example.twitchclient.ui.channel.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.videos.VideoInfo

class VideoDiffUtils(
    private val oldList: List<VideoInfo>,
    private val newList: List<VideoInfo>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldItemPosition == newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                (oldList[oldItemPosition].id == newList[newItemPosition].id) ||
                        (oldList[oldItemPosition].title) == newList[newItemPosition].title)
    }
}