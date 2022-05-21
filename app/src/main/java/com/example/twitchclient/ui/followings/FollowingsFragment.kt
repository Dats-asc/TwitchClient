package com.example.twitchclient.ui.followings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.ui.followings.recycler.StreamAdapter
import com.example.twitchclient.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class FollowingsFragment : Fragment() {

    private lateinit var binding: FollowingsFragmentBinding

    private var streamAdapter: StreamAdapter? = null

    private val viewModel: FollowingsViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FollowingsFragmentBinding.inflate(inflater, container, false).let {
        binding = FollowingsFragmentBinding.inflate(inflater, container, false)
        with(binding.toolbar) {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_search -> {
                        findNavController().navigate(R.id.action_navigation_followings_to_action_search)
                        true
                    }
                    R.id.action_auth -> {
                        viewModel.logout()
                        findNavController().navigate(R.id.action_navigation_followings_to_startFragment)
                        true
                    }
                    else -> false
                }
            }
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.isAuthorized) {
            initIfAuthorized()
        } else initIfNotAuthorized()
    }

    private fun initIfAuthorized() {
        initAdapter()
        initObservers()
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getFollowedStreams()
    }

    private fun initIfNotAuthorized() {
        binding.followMessage.visibility = View.VISIBLE
        binding.progressbar.visibility = View.GONE
    }

    private fun initObservers() {
        viewModel.queryStreams.observe(viewLifecycleOwner) {
            it.fold(
                onSuccess = { streams ->
                    onStreamsLoad(streams.streams)
                }, onFailure = {
                    Snackbar.make(binding.root, "Something go wrong", Snackbar.LENGTH_LONG).show()
                    Log.e("", it.message.toString())
                }
            )
        }
    }

    private fun onStreamsLoad(streams: ArrayList<StreamData>) {
        streamAdapter?.updateData(streams)
        binding.progressbar.visibility = View.GONE
    }

    private fun initAdapter() {
        streamAdapter = StreamAdapter(
            arrayListOf(),
            onItemClicked = { streamData ->
                findNavController().navigate(
                    R.id.action_followingsFragment_to_streamFragment,
                    bundleOf(C.BROADCASTER_LOGIN to streamData.user_login)
                )
            },
            onChannelAvatarClicked = { streamData ->
                findNavController().navigate(
                    R.id.action_navigation_followings_to_channelDetailFragment,
                    bundleOf(C.USER_ID to streamData.id)
                )
            },
            onNextStreams = {

            })
        binding.rvStreams.adapter = streamAdapter
    }

}