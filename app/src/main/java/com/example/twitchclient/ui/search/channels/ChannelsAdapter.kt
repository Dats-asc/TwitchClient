package com.example.twitchclient.ui.search.channels

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.domain.entity.search.ChannelInfo
import com.example.twitchclient.domain.entity.search.Channels

class ChannelsAdapter(
    private val channels: ArrayList<ChannelInfo>,
    private val onItemClicked: (String) -> Unit,
    private val onNextChannels: () -> Unit
) : RecyclerView.Adapter<ChannelsHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChannelsHolder = ChannelsHolder.create(parent, onItemClicked)

    override fun onBindViewHolder(holder: ChannelsHolder, position: Int) {
        holder.bind(channels[position])
        if (position < channels.size - 1 && position > channels.size - 3) {
            onNextChannels()
        }
    }

    fun updateData(newData: ArrayList<ChannelInfo>) {
        val callback = ChannelDiffUtils(channels, newData)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        channels.clear()
        channels.addAll(newData)
    }

    fun addNewChannels(newData: ArrayList<ChannelInfo>) {
        var newList = arrayListOf<ChannelInfo>()
        newList.addAll(channels)
        newList.addAll(newData)
        val callback = ChannelDiffUtils(channels, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        channels.addAll(newData)
    }

    override fun getItemCount(): Int = channels.size
}