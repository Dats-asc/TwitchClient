package com.example.twitchclient.ui.channel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.twitchclient.C
import com.example.twitchclient.databinding.FragmentChannelDetailBinding
import com.example.twitchclient.domain.entity.user.UserDetail
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.ui.channel.recycler.VideoAdapter
import com.example.twitchclient.ui.main.MainActivity


class ChannelDetailFragment : Fragment() {

    private lateinit var binding: FragmentChannelDetailBinding

    private lateinit var userId: String

    private lateinit var videoAdapter: VideoAdapter

    private val viewModel: ChannelDetailViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentChannelDetailBinding.inflate(inflater, container, false).let {
        binding = FragmentChannelDetailBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        userId = arguments?.getString(C.USER_ID).orEmpty()
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initObservers()
        viewModel.getUserDetail(userId)
        viewModel.getChannelVideos(userId)
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun initObservers() {
        viewModel.userDetail.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { userDetail ->
                onUserDetail(userDetail)
            }, onFailure = {
                Log.e("", it.message.orEmpty())
            })
        }
        viewModel.videos.observe(viewLifecycleOwner) {
            it.fold(onSuccess = { videos ->
                onVideoResponse(videos)
            }, onFailure = {
                Toast.makeText(requireActivity(), it.message.orEmpty(), Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun onVideoResponse(videos: ArrayList<VideoInfo>) {
        videoAdapter.updateData(videos)
        binding.progressbar.visibility = View.GONE
        binding.recyclerProgressbar.visibility = View.GONE
    }

    private fun onUserDetail(user: UserDetail) {
        with(binding) {
            if (!user.offline_image_url.isNullOrEmpty()) {
                Glide.with(requireActivity())
                    .load(user.offline_image_url)
                    .into(offlineImage)
            }
            Glide.with(requireActivity())
                .load(user.profile_image_url)
                .into(channelIcon)
            tvChannelName.text = user.display_name
            tvCreateDate.text = "Дата создания: ${user.created_at.split("T")[0]}"
            tvViewsCount.text = "Просмотров: ${user.view_count}"
            tvDescription.text = user.description
            if (user.is_live) {
                tvViewersCount.text = "Зрителей: ${user.viewer_count}"
                tvStreamTime.text = "Время начала: ${user.started_at.split("T")[1]}"
            }
        }
    }

    private fun initAdapter() {
        videoAdapter = VideoAdapter(
            arrayListOf(),
            onItemClicked = {

            },
            onNextStreams = {
                viewModel.getNextVideos(userId)
                binding.recyclerProgressbar.visibility = View.VISIBLE
            }
        )
        binding.rvVideos.adapter = videoAdapter
    }
}