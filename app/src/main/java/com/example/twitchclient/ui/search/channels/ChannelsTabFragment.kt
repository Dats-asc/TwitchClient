package com.example.twitchclient.ui.search.channels

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentChannelsTabBinding
import com.example.twitchclient.domain.entity.search.Channels
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.search.SearchableTab
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChannelsTabFragment : Fragment(), SearchableTab {

    private lateinit var recyclerView: RecyclerView

    private lateinit var binding: FragmentChannelsTabBinding

    private var channelsAdapter: ChannelsAdapter? = null

    private val viewModel: ChannelsTabViewModel by viewModels {
        (activity as MainActivity).factory
    }

    private var requestFlag = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentChannelsTabBinding.inflate(inflater, container, false).let {
        binding = FragmentChannelsTabBinding.inflate(inflater, container, false)
        binding.root
    }

    override fun onStart() {
        super.onStart()
        initObservers()
        initAdapter()
    }

    private fun initObservers() {
        viewModel.queryChannels.observe(requireActivity()) {
            it.fold(
                onSuccess = { channels ->
                    onSearchResponse(channels)
                    binding.progressbar.visibility = View.GONE
                },
                onFailure = { e ->
                    Log.e("", e.message.orEmpty())
                }
            )
        }
        viewModel.queryNextChannels.observe(requireActivity()) {
            it.fold(
                onSuccess = { channels ->
                    onNextChannelsLoad(channels)
                }, onFailure = {
                    Snackbar.make(binding.root, it.message.orEmpty(), Snackbar.LENGTH_LONG).show()
                }
            )
        }
    }

    private fun updateRequestFlag() {
        lifecycleScope.launch {
            requestFlag = false
            delay(250)
            requestFlag = true
        }
    }

    private fun onSearchResponse(channels: Channels) {
        while (requestFlag) {
            channelsAdapter?.updateData(channels.channels)
            if (channels.channels.isEmpty()) {
                binding.tvNothingFindMessage.visibility = View.VISIBLE
            } else
                binding.tvNothingFindMessage.visibility = View.GONE
            updateRequestFlag()
        }
    }

    private fun initAdapter() {
        recyclerView = requireActivity().findViewById(R.id.rv_channels)
        channelsAdapter = ChannelsAdapter(
            arrayListOf(),
            onItemClicked = {

            }, onNextChannels = {
                viewModel.onNextChannels()
                binding.recyclerProgressbar.visibility = View.VISIBLE
            })
        recyclerView.adapter = channelsAdapter
        requireActivity().findViewById<TextView>(R.id.tv_nothing_find_message).visibility =
            View.GONE
    }

    private fun onNextChannelsLoad(channels: Channels) {
        channelsAdapter?.addNewChannels(channels.channels)
        binding.recyclerProgressbar.visibility = View.GONE
        binding.rvChannels.scrollToPosition(channelsAdapter?.itemCount ?: 0 - 19)
    }

    override fun onSearch(request: String) {
        requireActivity().findViewById<ProgressBar>(R.id.progressbar).visibility = View.VISIBLE
        viewModel.onFirstQuery(request)
    }
}