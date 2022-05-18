package com.example.twitchclient.ui.followings

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.StreamItem
import com.example.twitchclient.domain.entity.streams.Streams
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
            setupWithNavController(findNavController())
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_search -> {
                        findNavController().navigate(R.id.action_navigation_followings_to_action_search)
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
        initAdapter()
        initObservers()
        binding.progressbar.visibility = View.VISIBLE
        viewModel.getFollowedStreams()
    }

    private fun initObservers() {
        viewModel.queryStreams.observe(viewLifecycleOwner) {
            it.fold(
                onSuccess = { streams ->
                    onStreamsLoad(streams.streams)
                    binding.progressbar.visibility = View.GONE
                }, onFailure = {
                    Snackbar.make(binding.root, "Something go wrong", Snackbar.LENGTH_LONG).show()
                    Log.e("", it.message.toString())
                }
            )
        }
    }

    private fun onStreamsLoad(streams: ArrayList<StreamData>) {
        streamAdapter?.updateData(streams)
    }

    private fun initAdapter() {
        streamAdapter = StreamAdapter(arrayListOf(), onItemClicked = { streamData ->
            findNavController().navigate(
                R.id.action_followingsFragment_to_streamFragment,
                bundleOf(C.BROADCASTER_LOGIN to streamData.user_login)
            )
        }, onNextStreams = {

        })
        binding.rvStreams.adapter = streamAdapter
    }

}