package com.example.twitchclient.ui.search.games

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.domain.entity.search.GameInfo

class GamesAdapter(
    private val games: ArrayList<GameInfo>,
    private val onItemClick: (String) -> Unit,
    private val onNextChannels: () -> Unit
) : RecyclerView.Adapter<GamesHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GamesHolder = GamesHolder.create(parent, onItemClick)

    override fun onBindViewHolder(holder: GamesHolder, position: Int) {
        holder.bind(games[position])
        if (position < games.size - 1 && position > games.size - 3) {
            onNextChannels()
        }
    }

    fun updateData(newData: ArrayList<GameInfo>) {
        val callback = GamesDiffUtils(games, newData)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        games.clear()
        games.addAll(newData)
    }

    fun addNewGames(newData: ArrayList<GameInfo>) {
        var newList = arrayListOf<GameInfo>()
        newList.addAll(games)
        newList.addAll(newData)
        val callback = GamesDiffUtils(games, newList)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        games.addAll(newData)
    }

    fun clearData(){
        val callback = GamesDiffUtils(arrayListOf(), arrayListOf())
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        games.clear()
    }

    override fun getItemCount(): Int = games.size
}