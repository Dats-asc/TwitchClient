package com.example.twitchclient.ui.followings

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.domain.entity.search.ChannelInfo
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.StreamItem
import com.example.twitchclient.ui.search.channels.ChannelDiffUtils

class StreamAdapter(
    private val streams: ArrayList<StreamData>,
    private val onItemClicked: (StreamData) -> Unit,
    private val onNextStreams: () -> Unit
) : RecyclerView.Adapter<StreamHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StreamHolder = StreamHolder.create(parent, onItemClicked)

    override fun onBindViewHolder(holder: StreamHolder, position: Int) {
        holder.bind(streams[position])
        if (position < streams.size - 1 && position > streams.size - 3) {
            onNextStreams()
        }
    }

    fun updateData(newData: ArrayList<StreamData>) {
        val callback = StreamDiffUtils(streams, newData)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        streams.clear()
        streams.addAll(newData)
    }

    fun nextStreams(newData: ArrayList<StreamData>) {
        var newList = arrayListOf<StreamData>()
        newList.addAll(streams)
        newList.addAll(newData)
        val callback = StreamDiffUtils(streams, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        streams.addAll(newData)
    }

    override fun getItemCount(): Int = streams.size
}