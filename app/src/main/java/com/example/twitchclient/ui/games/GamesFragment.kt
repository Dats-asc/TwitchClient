package com.example.twitchclient.ui.games

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.twitchclient.R
import com.example.twitchclient.data.api.mapper.TwitchMapper
import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.databinding.GamesFragmentBinding
import com.example.twitchclient.domain.entity.search.GameInfo
import com.example.twitchclient.domain.usecases.twitch.PingUserUseCase
import com.example.twitchclient.ui.chat.ChatFragment
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.navigation.navigator
import com.example.twitchclient.ui.popular.PopularViewModel
import com.example.twitchclient.ui.search.games.GamesAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class GamesFragment : Fragment() {

    private lateinit var binding: GamesFragmentBinding

    private var gamesAdapter: GamesAdapter? = null

    private val viewModel: GamesViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = GamesFragmentBinding.inflate(inflater, container, false).let {
        binding = it
        with(binding.toolbar) {
            setupWithNavController(
                findNavController(),
                AppBarConfiguration(
                    setOf(
                        R.id.navigation_followings,
                        R.id.navigation_popular,
                        R.id.navigation_games
                    )
                )
            )
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_search -> {
                        findNavController().navigate(R.id.action_search)
                        true
                    }
                    else -> false
                }
            }
        }
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initAdapter()
        initObservers()
        viewModel.getTopGames()
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun initObservers() {
        viewModel.games.observe(viewLifecycleOwner) {
            it.fold(
                onSuccess = { games ->
                    onGamesResponse(games)
                }, onFailure = {
                    val a = 0
                    Snackbar.make(binding.root, it.message.orEmpty(), Snackbar.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun onGamesResponse(games: ArrayList<GameInfo>) {
        gamesAdapter?.updateData(games)
        binding.progressbar.visibility = View.GONE
        binding.recyclerProgressbar.visibility = View.GONE

    }

    private fun initAdapter() {
        gamesAdapter = GamesAdapter(
            arrayListOf(),
            onItemClick = {

            },
            onNextGames = {
                viewModel.getNextGames()
                binding.recyclerProgressbar.visibility = View.VISIBLE
            }
        )
        binding.rvGames.adapter = gamesAdapter
    }

}