package com.example.twitchclient.ui.followings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.ui.auth.AuthFragment
import com.example.twitchclient.ui.navigation.navigator


class FollowingsFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingsFragment()
    }

    private lateinit var viewModel: FollowingsViewModel

    private lateinit var binding: FollowingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FollowingsFragmentBinding.inflate(inflater, container, false)?.let {
        binding = FollowingsFragmentBinding.inflate(inflater, container, false)
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignin.setOnClickListener {
            navigator().pushFragment(AuthFragment())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowingsViewModel::class.java)
    }

}