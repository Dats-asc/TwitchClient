package com.example.twitchclient.ui.followings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.twitchclient.C
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.navigation.NavOption
import com.example.twitchclient.ui.navigation.navigator
import com.example.twitchclient.ui.stream.StreamFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Singleton

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
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.savedData?.let {
            initAdapter(it)
        } ?: kotlin.run {
            initObservers()
            viewModel.getFollowedStreams()
        }
    }

    private fun initObservers() {
        viewModel.queryStreams.observe(requireActivity()) {
            it.fold(
                onSuccess = { streams ->
                    initAdapter(streams)
                    viewModel.savedData = streams
                }, onFailure = {
                    Snackbar.make(binding.root, "Something go wrong", Snackbar.LENGTH_LONG).show()
                    Log.e("", it.message.toString())
                }
            )
        }
    }

    private fun initAdapter(data: Streams){
        streamAdapter = StreamAdapter(data.data) { streamData ->
            navigator().pushFragment(
                StreamFragment().apply {
                    arguments = bundleOf(C.BROADCASTER_LOGIN to streamData.user_login)
                },
                NavOption.OPTION_HIDE_TOOLBAR_AND_BOTTOM_NAV_VIEW
            )

        }
        binding.rvStreams.adapter = streamAdapter
    }

}