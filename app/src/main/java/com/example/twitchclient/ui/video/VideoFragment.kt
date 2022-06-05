package com.example.twitchclient.ui.video

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.FragmentVideoBinding
import com.example.twitchclient.domain.entity.videos.VideoInfo
import com.example.twitchclient.domain.entity.videos.VideoPlaylist
import com.example.twitchclient.ui.main.MainActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class VideoFragment : Fragment() {

    private lateinit var binding: FragmentVideoBinding

    private var videoId: String? = null

    private lateinit var videoInfo: VideoInfo

    private lateinit var player: ExoPlayer

    private var videoPlaylist: VideoPlaylist? = null

    private val viewModel: VideoViewModel by viewModels {
        (activity as MainActivity).factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentVideoBinding.inflate(inflater, container, false).let {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        videoId = arguments?.getString(C.VIDEO_ID)
        videoInfo = arguments?.getParcelable(C.VIDEO_INFO)!!
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!videoId.isNullOrEmpty()){
            viewModel.start(videoInfo, PlayerType.ONLINE)
        } else{
            viewModel.start(videoInfo, PlayerType.OFFLINE)
        }
        initObservers()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val constraintSet = ConstraintSet()
            constraintSet.connect(binding.videoPlayerView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.connect(binding.videoPlayerView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            constraintSet.connect(binding.videoPlayerView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(binding.videoPlayerView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.applyTo(binding.constraintRoot)
//            hideWindowUi()
        } else{
            val constraintSet = ConstraintSet()
            constraintSet.connect(binding.videoPlayerView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.connect(binding.videoPlayerView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(binding.videoPlayerView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.applyTo(binding.constraintRoot)
//            showWindowUi()
            val layoutParams = binding.videoPlayerView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.dimensionRatio = "16:9"
        }

        activity?.window?.decorView?.requestLayout()
    }

    private fun initObservers() {
        viewModel.videoPlaylist.observe(viewLifecycleOwner) {
            videoPlaylist = it
            onVideoPlaylistResponse()
        }
        viewModel.videoPlayer.observe(viewLifecycleOwner) { player ->
            this.player = player
            initPlayer(player)
            initPlayerUi()
        }
    }

    private fun onVideoPlaylistResponse() {
        activity?.findViewById<ImageView>(R.id.btn_settings)?.setOnClickListener {
            onSetVideoQualityOptionClicked()
        }
    }

    private fun onSetVideoQualityOptionClicked() {
        val optionsList = videoPlaylist?.qualities?.toTypedArray()
        var option = 0

        AlertDialog.Builder(requireContext())
            .setTitle("Выберите качество видео: ")
            .setNeutralButton("Отмена") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Ok") { dialog, which ->
                onQualityPicked(option)
                dialog.dismiss()
            }
            .setSingleChoiceItems(optionsList, option) { dialog, which ->
                option = which
            }
            .show()
    }

    private fun onQualityPicked(option: Int) {
        videoPlaylist?.let {
            viewModel.setQualityOption(it.urls[option])
        }
    }

    private fun hideWindowUi() {
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
    }

    private fun showWindowUi(){
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    private fun initPlayerUi() {
        activity?.let {

            val btnBack = it.findViewById<ImageView>(R.id.btn_pop_up)
            val seekbar = it.findViewById<SeekBar>(R.id.seekbar)
            val btnPlay = it.findViewById<ImageView>(R.id.btn_play_pause)
            val btnForward = it.findViewById<ImageView>(R.id.btn_forward)
            val btnReplay = it.findViewById<ImageView>(R.id.btn_replay)
            val tvVideoDuration = it.findViewById<TextView>(R.id.tv_video_duration)
            val tvCurrentPosition = it.findViewById<TextView>(R.id.tv_current_position)

            var isNotStartTrackingSeekbar = true
            var currentPosition = 0L
            var seekBarPosition = 0
            tvVideoDuration.text = convertMillisToTime(player.duration)
            tvCurrentPosition.text = convertMillisToTime(0)

            btnBack.setOnClickListener { findNavController().navigateUp() }

            btnPlay.setOnClickListener {
                if (viewModel.isPlaying == true) {
                    viewModel.pause()
                    btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                } else {
                    viewModel.play()
                    btnPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }

            btnForward.setOnClickListener {
                player.seekTo(player.currentPosition + 10_000)
            }

            btnReplay.setOnClickListener {
                player.seekTo(player.currentPosition - 10_000)
            }

            lifecycleScope.launch {
                while (true) {
                    if (isNotStartTrackingSeekbar) {
                        tvCurrentPosition.text = convertMillisToTime(player.currentPosition)
                        tvVideoDuration.text = convertMillisToTime(player.duration)
                    }
                    delay(250)
                }
            }

            seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    seekBarPosition = p1
                    tvCurrentPosition.text =
                        convertMillisToTime((player.duration / 100) * seekBarPosition)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    isNotStartTrackingSeekbar = false
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    currentPosition = (player.duration / 100) * seekBarPosition
                    player.seekTo(currentPosition)
                    seekbar.progress = seekBarPosition
                    isNotStartTrackingSeekbar = true
                }

            })
        }
    }

    private fun initPlayer(player: ExoPlayer) {
        this.player = player
        binding.videoPlayerView.player = player
    }

    private fun convertMillisToTime(millis: Long): String {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.US)
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"))
        return formatter.format(Date(millis))
    }
}