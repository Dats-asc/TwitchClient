package com.example.twitchclient.ui.videos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentVideosBinding
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.ui.channel.recycler.VideoAdapter
import com.example.twitchclient.ui.games.GamesViewModel
import com.example.twitchclient.ui.main.MainActivity

class VideosFragment : Fragment() {

    private lateinit var binding: FragmentVideosBinding

    private lateinit var videosAdapter: VideoAdapter

    private val viewModel: VideosViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentVideosBinding.inflate(inflater, container, false).let {
        binding = FragmentVideosBinding.inflate(layoutInflater, container, false)
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressbar.visibility = View.VISIBLE
        initObservers()
        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedVideos()
    }

    private fun initObservers() {
        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            setVideos(videos)
        }
    }

    private fun setVideos(videos: ArrayList<VideoInfo>) {
        videosAdapter.updateData(videos)
        binding.progressbar.visibility = View.GONE
    }

    private fun initAdapter() {
        videosAdapter = VideoAdapter(
            arrayListOf(),
            onItemClicked = {

            },
            onNextVideo = {},
            onItemMenuClicked = { video, view ->
                showPopup(view, video)
            }
        )
        binding.rvVideos.adapter = videosAdapter
    }

    private fun showPopup(v: View, video: VideoInfo) {
        val popup = PopupMenu(requireActivity(), v)
        popup.setOnMenuItemClickListener {
            viewModel.deleteVideo(video.id)
            true
        }
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.saved_video_item_menu, popup.menu)
        popup.show()
    }

}