package com.example.twitchclient.ui.search.games

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentGamesTabBinding
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.search.SearchableTab
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamesTabFragment : Fragment(), SearchableTab {

    private lateinit var binding: FragmentGamesTabBinding

    private var gamesAdapter: GamesAdapter? = null

    private var requestFlag = true

    private val viewModel: GamesTabViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentGamesTabBinding.inflate(inflater, container, false).let {
        binding = FragmentGamesTabBinding.inflate(inflater, container, false)
        binding.root
    }

    override fun onStart() {
        super.onStart()
        initObservers()
        initAdapter()
    }

    override fun onSearch(request: String) {
        if (request.isEmpty()){
            gamesAdapter?.clearData()
        } else{
            viewModel.onFirstQuery(request)
            requireActivity().findViewById<ProgressBar>(R.id.progressbar).visibility = View.VISIBLE
        }
    }

    private fun initObservers() {
        viewModel.queryGames.observe(requireActivity()) {
            it.fold(onSuccess = { games ->
                onSearchResponse(games)
                binding.progressbar.visibility = View.GONE
            }, onFailure = { e ->
                Snackbar.make(binding.root, e.message.orEmpty(), Snackbar.LENGTH_SHORT)
            })
        }
        viewModel.queryNextGames.observe(requireActivity()) {
            it.fold(onSuccess = { nextGames ->
                onNextGames(nextGames)
                binding.recyclerProgressbar.visibility = View.GONE
            }, onFailure = { e ->
                Snackbar.make(binding.root, e.message.orEmpty(), Snackbar.LENGTH_SHORT)
            })
        }
    }

    private fun onSearchResponse(games: Games) {
        while (requestFlag) {
            gamesAdapter?.updateData(games.games)
            updateRequestFlag()
        }
    }

    private fun onNextGames(games: Games) {
        gamesAdapter?.addNewGames(games.games)
        binding.recyclerProgressbar.visibility = View.GONE
        binding.rvGames.scrollToPosition(gamesAdapter?.itemCount ?: 0 - 19)
    }

    private fun initAdapter() {
        gamesAdapter = GamesAdapter(
            arrayListOf(),
            onItemClick = {
                //TODO
            },
            onNextChannels = {
                viewModel.onNextChannels()
                binding.recyclerProgressbar.visibility = View.GONE
            })
        binding.rvGames.adapter = gamesAdapter
    }

    private fun updateRequestFlag() {
        lifecycleScope.launch {
            requestFlag = false
            delay(250)
            requestFlag = true
        }
    }
}