package com.example.twitchclient.ui.search.channels

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentChannelsTabBinding
import com.example.twitchclient.domain.entity.search.ChannelInfo
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.search.SearchableTab
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChannelsTabFragment : Fragment(), SearchableTab {

    private lateinit var binding: FragmentChannelsTabBinding

    private var channelsAdapter: ChannelsAdapter? = null

    private val viewModel: ChannelsTabViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentChannelsTabBinding.inflate(inflater, container, false).let {
        binding = FragmentChannelsTabBinding.inflate(inflater, container, false)
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initAdapter()
    }

    private fun initObservers() {
        viewModel.channels.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { channels ->
                if (channels.isNotEmpty())
                    onSearchResponse(channels)
            }, onFailure = {
                Snackbar.make(binding.root, it.message.orEmpty(), Snackbar.LENGTH_LONG).show()
            })
        }
    }

    override fun search(request: String) {
        if (request.isEmpty()) {
            channelsAdapter?.updateData(arrayListOf())
        } else {
            binding.progressbar.visibility = View.VISIBLE
            viewModel.getChannels(request)
        }
    }

    private fun onSearchResponse(channels: ArrayList<ChannelInfo>) {
        channelsAdapter?.updateData(channels)
        binding.progressbar.visibility = View.GONE
        binding.recyclerProgressbar.visibility = View.GONE
    }

    private fun initAdapter() {
        channelsAdapter = ChannelsAdapter(
            arrayListOf(),
            onItemClicked = { userId ->
                findNavController().navigate(
                    R.id.action_action_search_to_channelDetailFragment,
                    bundleOf(C.USER_ID to userId)
                )
            }, onNextChannels = {
                viewModel.getNextChannels()
                binding.recyclerProgressbar.visibility = View.VISIBLE
            })
        binding.rvChannels.adapter = channelsAdapter
    }
}