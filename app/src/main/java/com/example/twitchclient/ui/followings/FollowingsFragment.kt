package com.example.twitchclient.ui.followings

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.ui.auth.AuthFragment
import com.example.twitchclient.ui.auth.AuthorizationActivity

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
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignin.setOnClickListener {
            parentFragmentManager.beginTransaction().run {
                add(R.id.auth_fragment_container, AuthFragment())
                commit()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowingsViewModel::class.java)
    }

}