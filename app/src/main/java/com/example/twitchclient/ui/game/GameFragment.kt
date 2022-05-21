package com.example.twitchclient.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentGameBinding
import com.example.twitchclient.domain.entity.search.GameInfo
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.ui.followings.recycler.StreamAdapter
import com.example.twitchclient.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    private lateinit var gameId: String

    private lateinit var streamAdapter: StreamAdapter

    private val viewModel: GameViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentGameBinding.inflate(inflater, container, false).let {
        binding = it
        gameId = arguments?.getString(C.GAME_ID).orEmpty()
        init()
        binding.toolbar.apply {
            setNavigationOnClickListener { findNavController().navigateUp() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_search -> {
                        findNavController().navigate(R.id.action_gameFragment_to_action_search)
                        true
                    }
                    else -> false
                }
            }
        }
        binding.root
    }

    private fun init() {
        initObservers()
        initAdapter()
        viewModel.getGame(gameId)
        viewModel.getStreams(gameId)
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun initObservers() {
        viewModel.game.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { game ->
                initGame(game)
            }, onFailure = {
                Snackbar.make(binding.root, "Game not found", Snackbar.LENGTH_SHORT).show()
            })
        }
        viewModel.streams.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { streams ->
                onStreamResponse(streams)
            }, onFailure = {

            })
        }
    }

    private fun onStreamResponse(streams: ArrayList<StreamData>) {
        streamAdapter.updateData(streams)
        binding.progressbar.visibility = View.GONE
        binding.recyclerProgressbar.visibility = View.GONE
    }

    private fun initGame(game: GameInfo) {
        Glide.with(this)
            .load(game.box_art_url)
            .into(binding.gameBoxArt)
        binding.tvGameTitle.text = game.name
    }

    private fun initAdapter() {
        streamAdapter = StreamAdapter(
            arrayListOf(),
            onItemClicked = { streamData ->
                findNavController().navigate(
                    R.id.action_gameFragment_to_streamFragment,
                    bundleOf(C.BROADCASTER_LOGIN to streamData.user_login)
                )
            }, onChannelAvatarClicked = { streamData ->
                findNavController().navigate(
                    R.id.action_gameFragment_to_channelDetailFragment,
                    bundleOf(C.USER_ID to streamData.id)
                )
            },
            onNextStreams = {
                viewModel.getNextStreams()
                binding.recyclerProgressbar.visibility = View.VISIBLE
            })
        binding.rvStreams.adapter = streamAdapter
    }
}