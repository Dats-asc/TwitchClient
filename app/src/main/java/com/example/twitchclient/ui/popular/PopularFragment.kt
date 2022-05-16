package com.example.twitchclient.ui.popular

import android.os.Bundle
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
import com.example.twitchclient.databinding.PopularFragmentBinding
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.ui.followings.StreamAdapter
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
        binding.toolbar.setupWithNavController(findNavController())
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
        viewModel.queryStreams.observe(requireActivity()) {
            it.fold(onSuccess = { streams ->
                onStreamsResponse(streams)
            }, onFailure = {
                Snackbar.make(binding.root, it.message.orEmpty(), Snackbar.LENGTH_SHORT)
            })
        }
        viewModel.nextStreams.observe(requireActivity()) {
            it.fold(onSuccess = { streams ->
                onNextStreams(streams)
            }, onFailure = {
                Snackbar.make(binding.root, it.message.orEmpty(), Snackbar.LENGTH_SHORT)
            })
        }
    }

    private fun onStreamsResponse(streams: Streams) {
        streamAdapter?.updateData(streams.streams)
        binding.progressbar.visibility = View.GONE
    }

    private fun onNextStreams(streams: Streams) {
        streamAdapter?.nextStreams(streams.streams)
        binding.recyclerProgressbar.visibility = View.GONE
        binding.rvStreams.scrollToPosition(streamAdapter?.itemCount ?: 0 - 19)
    }

    private fun initAdapter() {
        streamAdapter = StreamAdapter(
            arrayListOf(),
            onItemClicked = { streamData ->
//                findNavController().navigate(
//                    R.id.action_navigation_popular_to_streamFragment,
//                    bundleOf(C.BROADCASTER_LOGIN to streamData.user_login)
//                )

                findNavController().navigate(
                    R.id.streamFragment,
                    bundleOf(C.BROADCASTER_LOGIN to streamData.user_login)
                )
            },
            onNextStreams = {
                viewModel.getNextStreams()
                binding.recyclerProgressbar.visibility = View.VISIBLE
            })
        binding.rvStreams.adapter = streamAdapter
    }

}