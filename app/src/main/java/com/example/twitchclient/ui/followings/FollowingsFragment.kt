package com.example.twitchclient.ui.followings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.twitchclient.data.api.mapper.TwitchMapper
import com.example.twitchclient.data.repository.TwitchRepositoryImpl
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.domain.usecases.twitch.GetFollowedStreamsUseCase
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject


class FollowingsFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingsFragment()
    }

    private lateinit var binding: FollowingsFragmentBinding

    private var streamAdapter: StreamAdapter? = null

    private val viewModel: FollowingsViewModel by viewModels{
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FollowingsFragmentBinding.inflate(inflater, container, false)?.let {
        binding = FollowingsFragmentBinding.inflate(inflater, container, false)
        initObservers()
        viewModel.getFolloweStreams()
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnSignin.setOnClickListener {
//            navigator().pushFragment(AuthFragment())
//        }
    }

    private fun initObservers(){
        viewModel.queryStreams.observe(activity as MainActivity){
            it.fold(
                onSuccess = { streams ->
                    streamAdapter = StreamAdapter(streams.data, {
                        //TODO on recyclerview item click listener
                    })
                    binding.rvStreams.adapter = streamAdapter
                }, onFailure = {
                    Snackbar.make(binding.root, "Fail", Snackbar.LENGTH_LONG).show()
                }
            )
        }
    }

}