package com.example.twitchclient.ui.followings

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.StreamItem

class StreamDiffUtils(
    private val oldList: List<StreamData>,
    private val newList: List<StreamData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldItemPosition == newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == newList[newItemPosition].id)
    }
}