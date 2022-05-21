package com.example.twitchclient.ui.search.games

import androidx.recyclerview.widget.DiffUtil
import com.example.twitchclient.domain.entity.search.GameInfo

class GamesDiffUtils(
    private val oldList: List<GameInfo>,
    private val newList: List<GameInfo>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldItemPosition == newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return ((oldList[oldItemPosition].id == newList[newItemPosition].id) ||
                (oldList[oldItemPosition].name == newList[newItemPosition].name))
    }
}