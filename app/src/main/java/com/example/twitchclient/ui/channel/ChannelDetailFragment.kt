package com.example.twitchclient.ui.channel

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentChannelDetailBinding
import com.example.twitchclient.domain.entity.user.UserDetail
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.ui.channel.recycler.VideoAdapter
import com.example.twitchclient.ui.main.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar


class ChannelDetailFragment : Fragment() {

    private lateinit var binding: FragmentChannelDetailBinding

    private lateinit var userId: String

    private lateinit var videoAdapter: VideoAdapter

    private val viewModel: ChannelDetailViewModel by viewModels {
        (activity as MainActivity).factory
    }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
            } else {
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentChannelDetailBinding.inflate(inflater, container, false).let {
        binding = FragmentChannelDetailBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        userId = arguments?.getString(C.USER_ID).orEmpty()
        binding.collapsingToolbarLayout.let {
        }
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
        viewModel.videosDb.observe(viewLifecycleOwner) {
            it.fold(onSuccess = {
                it
                val a = 0
            }, onFailure = {

            }
            )
        }
        viewModel.isSaved.observe(viewLifecycleOwner) { videoIsSaved ->
            if (videoIsSaved) {
                Snackbar.make(binding.root, "Видео уже сохранено", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Видео уже сохранено", Snackbar.LENGTH_SHORT).show()
            }
        }
        viewModel.test.observe(viewLifecycleOwner) {
            it.fold(onSuccess = {
                it
            }, onFailure = {
                it.message
                val a = 0
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
            toolbar.title = user.display_name
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
            btnWatch.setOnClickListener {
                findNavController().navigate(
                    R.id.action_channelDetailFragment_to_streamFragment,
                    bundleOf(C.BROADCASTER_LOGIN to user.login)
                )
            }
        }
    }

    private fun initAdapter() {
        videoAdapter = VideoAdapter(
            arrayListOf(),
            onItemClicked = { videoInfo ->
                findNavController().navigate(
                    R.id.action_channelDetailFragment_to_videoFragment,
                    bundleOf(C.VIDEO_ID to videoInfo.id)
                )
            },
            onNextVideo = {
                viewModel.getNextVideos(userId)
                binding.recyclerProgressbar.visibility = View.VISIBLE
            },
            onItemMenuClicked = { videoInfo, view ->
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    requestLocationAccess()
                }
                showPopup(view, videoInfo)
            }
        )
        binding.rvVideos.adapter = videoAdapter
    }

    private fun showPopup(v: View, video: VideoInfo) {
        val popup = PopupMenu(requireActivity(), v)
        popup.setOnMenuItemClickListener {
            viewModel.addVideo(video)
            true
        }
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.video_item_menu, popup.menu)
        popup.show()
    }

    private fun requestLocationAccess() {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}