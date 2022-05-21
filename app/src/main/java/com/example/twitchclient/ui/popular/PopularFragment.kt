package com.example.twitchclient.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.PopularFragmentBinding
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.ui.followings.recycler.StreamAdapter
import com.example.twitchclient.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class PopularFragment : Fragment() {

    private lateinit var binding: PopularFragmentBinding

    private var streamAdapter: StreamAdapter? = null

    private val viewModel: PopularViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = PopularFragmentBinding.inflate(inflater, container, false).let {
        binding = PopularFragmentBinding.inflate(inflater, container, false)
        with(binding.toolbar) {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_search -> {
                        findNavController().navigate(R.id.action_navigation_popular_to_action_search)
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
        initObservers()
        initAdapter()
        viewModel.getPopularStreams()
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun initObservers() {
        viewModel.streams.observe(viewLifecycleOwner) {
            it.fold(onSuccess = {
                onStreamsResponse(it)
            }, onFailure = {
                Snackbar.make(binding.root, it.message.orEmpty(), Snackbar.LENGTH_SHORT).show()
            })
        }
    }

    private fun onStreamsResponse(streams: ArrayList<StreamData>) {
        streamAdapter?.updateData(streams)
        binding.progressbar.visibility = View.GONE
        binding.recyclerProgressbar.visibility = View.GONE
    }

    private fun initAdapter() {
        streamAdapter = StreamAdapter(
            arrayListOf(),
            onItemClicked = { streamData ->
                findNavController().navigate(
                    R.id.action_navigation_popular_to_streamFragment,
                    bundleOf(C.BROADCASTER_LOGIN to streamData.user_login)
                )
            }, onChannelAvatarClicked = { streamData ->
                findNavController().navigate(
                    R.id.action_navigation_popular_to_channelDetailFragment,
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