package com.example.twitchclient.ui.search.channels

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchclient.domain.entity.search.ChannelInfo

class ChannelDiffUtils(
    private val oldList: List<ChannelInfo>,
    private val newList: List<ChannelInfo>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldItemPosition == newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].broadcaster_login == newList[newItemPosition].broadcaster_login)
    }
}