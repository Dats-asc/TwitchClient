package com.example.twitchclient.ui.followings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.twitchclient.data.api.mapper.TwitchMapper
import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.domain.entity.streams.StreamData
import com.example.twitchclient.domain.entity.streams.Streams
import com.example.twitchclient.domain.repository.TwitchRepository
import com.example.twitchclient.domain.usecases.twitch.GetFollowedStreamsUseCase
import com.example.twitchclient.ui.MainActivity
import com.example.twitchclient.ui.auth.AuthFragment
import com.example.twitchclient.ui.navigation.navigator
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import java.lang.Exception


class FollowingsFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingsFragment()
    }

    private lateinit var viewModel: FollowingsViewModel

    private lateinit var binding: FollowingsFragmentBinding

    private lateinit var getFollowedStreamsUseCase: GetFollowedStreamsUseCase

    private var streamAdapter: StreamAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FollowingsFragmentBinding.inflate(inflater, container, false)?.let {
        binding = FollowingsFragmentBinding.inflate(inflater, container, false)
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnSignin.setOnClickListener {
//            navigator().pushFragment(AuthFragment())
//        }

        initAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowingsViewModel::class.java)
    }

    private fun initAdapter() {
        getFollowedStreamsUseCase = GetFollowedStreamsUseCase(
            TwitchRepositoryImpl((activity as MainActivity).getAccessToken() ?: "", TwitchMapper())
        )
        lifecycleScope.launch {
            val streams = getFollowedStreamsUseCase.invoke("211258578")

            streamAdapter = StreamAdapter(streams!!.data) {

            }

            binding.rvStreams.adapter = streamAdapter
        }
    }

}