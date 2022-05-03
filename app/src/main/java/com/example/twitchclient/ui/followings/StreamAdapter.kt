package com.example.twitchclient.ui.followings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.domain.entity.streams.StreamData

class StreamAdapter (
    private val cities: List<StreamData>,
    private val action: (StreamData) -> Unit
) : RecyclerView.Adapter<StreamHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StreamHolder = StreamHolder.create(parent, action)

    override fun onBindViewHolder(holder: StreamHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int = cities.size
}