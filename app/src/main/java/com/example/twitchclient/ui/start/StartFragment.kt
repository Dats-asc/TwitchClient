package com.example.twitchclient.ui.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentStartBinding
import com.example.twitchclient.ui.followings.FollowingsViewModel
import com.example.twitchclient.ui.main.MainActivity

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding

    private val viewModel: StartViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentStartBinding.inflate(inflater, container, false).let {
        binding = FragmentStartBinding.inflate(inflater, container, false)
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(viewModel.isAuthorized){
            findNavController().navigate(R.id.action_startFragment_to_navigation_followings)
        } else{
            binding.btnSignIn.setOnClickListener{
                findNavController().navigate(R.id.action_startFragment_to_authFragment)
            }
        }
    }
}