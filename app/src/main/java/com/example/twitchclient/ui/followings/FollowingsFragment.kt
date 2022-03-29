package com.example.twitchclient.ui.followings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twitchclient.databinding.FollowingsFragmentBinding
import com.example.twitchclient.ui.MainActivity


class FollowingsFragment : Fragment() {

    companion object {
        fun newInstance() = FollowingsFragment()
    }

    private lateinit var viewModel: FollowingsViewModel

    private lateinit var binding: FollowingsFragmentBinding

    private var supportActionBar: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FollowingsFragmentBinding.inflate(inflater, container, false)?.let {
        binding = it
        supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignin.setOnClickListener {
            (activity as MainActivity?)?.onAuthFragmentOpen()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowingsViewModel::class.java)
    }

}