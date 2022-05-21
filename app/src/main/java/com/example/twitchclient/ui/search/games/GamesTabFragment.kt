package com.example.twitchclient.ui.search.games

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentGamesTabBinding
import com.example.twitchclient.domain.entity.search.GameInfo
import com.example.twitchclient.domain.entity.search.Games
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.search.SearchableTab
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamesTabFragment : Fragment(), SearchableTab {

    private lateinit var binding: FragmentGamesTabBinding

    private var gamesAdapter: GamesAdapter? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
    }


    override fun search(request: String) {
        if (request.isEmpty()) {
            gamesAdapter?.clear()
        } else {
            viewModel.getGames(request)
            binding.progressbar.visibility = View.VISIBLE
        }
    }

    private fun initObservers() {
        viewModel.games.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { games ->
                if (games.isNotEmpty()) {
                    onSearchResponse(games)
                } else {
                    binding.progressbar.visibility = View.GONE
                }
            }, onFailure = {
                Snackbar.make(binding.root, it.message.orEmpty(), Snackbar.LENGTH_SHORT).show()
            })
        }
    }

    private fun onSearchResponse(games: ArrayList<GameInfo>) {
        gamesAdapter?.updateData(games)
        binding.progressbar.visibility = View.GONE
        binding.recyclerProgressbar.visibility = View.GONE
    }

    private fun initAdapter() {
        gamesAdapter = GamesAdapter(
            arrayListOf(),
            onItemClick = { gameId ->
                findNavController().navigate(
                    R.id.action_action_search_to_gameFragment,
                    bundleOf(C.GAME_ID to gameId)
                )
            },
            onNextGames = {
                viewModel.getNextGames()
                binding.recyclerProgressbar.visibility = View.VISIBLE
            })
        binding.rvGames.adapter = gamesAdapter
    }
}