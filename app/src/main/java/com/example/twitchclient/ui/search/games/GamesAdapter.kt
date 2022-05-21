package com.example.twitchclient.ui.search.games

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.domain.entity.search.GameInfo

class GamesAdapter(
    private val games: ArrayList<GameInfo>,
    private val onItemClick: (String) -> Unit,
    private val onNextGames: () -> Unit
) : RecyclerView.Adapter<GamesHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GamesHolder = GamesHolder.create(parent, onItemClick)

    override fun onBindViewHolder(holder: GamesHolder, position: Int) {
        holder.bind(games[position])
        if (position < games.size - 1 && position > games.size - 3) {
            onNextGames()
        }
    }

    fun updateData(newData: ArrayList<GameInfo>) {
        val callback = GamesDiffUtils(games, newData)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
        games.clear()
        games.addAll(newData)
    }

    fun clear() {
        games.clear()
        val callback = GamesDiffUtils(games, arrayListOf())
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = games.size
}