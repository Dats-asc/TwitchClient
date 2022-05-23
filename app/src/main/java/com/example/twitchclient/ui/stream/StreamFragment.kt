package com.example.twitchclient.ui.stream

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.StreamFragmentBinding
import com.example.twitchclient.ui.chat.ChatFragment
import com.example.twitchclient.ui.main.MainActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class StreamFragment : Fragment() {

    private lateinit var binding: StreamFragmentBinding

    private lateinit var exoPlayerView: PlayerView

    private lateinit var broadcasterLogin: String

    private val viewModel: StreamViewModel by viewModels {
        (activity as MainActivity).factory
    }

    private lateinit var player: ExoPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = StreamFragmentBinding.inflate(inflater, container, false).let {
        binding = StreamFragmentBinding.inflate(inflater, container, false)
        arguments?.let { bundle ->
            broadcasterLogin = bundle.getString(C.BROADCASTER_LOGIN) ?: ""
        }
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChatContainer()
        startStream()
        initPlayerControl()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            val constraintSet = ConstraintSet()
            constraintSet.connect(binding.streamPlayerView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.connect(binding.streamPlayerView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            constraintSet.connect(binding.streamPlayerView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.constrainPercentWidth(R.id.stream_player_view, 0.7f)

            constraintSet.connect(binding.chatContainer.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.connect(binding.chatContainer.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            constraintSet.connect(binding.chatContainer.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.connect(binding.chatContainer.id, ConstraintSet.START, R.id.stream_player_view, ConstraintSet.END)
            constraintSet.constrainDefaultWidth(R.id.chat_container, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.applyTo(binding.constraintRoot)
            activity?.window?.decorView?.requestLayout()
        } else{
            val constraintSet = ConstraintSet()
            constraintSet.connect(binding.streamPlayerView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            constraintSet.connect(binding.streamPlayerView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(binding.streamPlayerView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.constrainDefaultWidth(R.id.stream_player_view, ConstraintSet.MATCH_CONSTRAINT)

            constraintSet.connect(binding.chatContainer.id, ConstraintSet.TOP, R.id.stream_player_view, ConstraintSet.BOTTOM)
            constraintSet.connect(binding.chatContainer.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(binding.chatContainer.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.connect(binding.chatContainer.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            constraintSet.constrainDefaultWidth(R.id.chat_container, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.constrainDefaultHeight(R.id.chat_container, ConstraintSet.MATCH_CONSTRAINT)
            constraintSet.applyTo(binding.constraintRoot)
            activity?.window?.decorView?.requestLayout()
            val layoutParams = binding.streamPlayerView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.dimensionRatio = "16:9"
        }
    }

    private fun initChatContainer() {
        with(binding) {
            childFragmentManager.beginTransaction().run {
                add(R.id.chat_container, ChatFragment().apply {
                    arguments = bundleOf(C.BROADCASTER_LOGIN to broadcasterLogin)
                })
                commit()
            }
        }
    }

    private fun initPlayerControl() {
        activity?.let {
            val btnPlay = it.findViewById<ImageView>(R.id.btn_play_pause)
            val btnGoLive = it.findViewById<LinearLayout>(R.id.btn_go_live)
            val btnFullScree = it.findViewById<ImageView>(R.id.btn_fullscreen)
            val tvBroadcasterName = it.findViewById<TextView>(R.id.tv_broadcaster_name)
            val btnSettings = it.findViewById<ImageView>(R.id.btn_settings)
            val btnPopUp = it.findViewById<ImageView>(R.id.btn_pop_up)

            tvBroadcasterName.text = broadcasterLogin

            btnPlay.setOnClickListener {
                if (viewModel.isPlaying()) {
                    viewModel.pause()
                    btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                } else {
                    viewModel.play()
                    btnPlay.setImageResource(R.drawable.ic_baseline_pause_24)
                }
            }

            btnGoLive.setOnClickListener {
                viewModel.restart()
                btnPlay.setImageResource(R.drawable.ic_baseline_pause_24)
            }

            btnSettings.setOnClickListener {
                onSetVideoQualityOptionClicked()
            }

            btnPopUp.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun onSetVideoQualityOptionClicked() {
        val optionsList = viewModel.getQualityOptionsList()?.toTypedArray()
        var option = 0

        AlertDialog.Builder(requireContext())
            .setTitle("Выберите качество видео: ")
            .setNeutralButton("Отмена") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Ok") { dialog, which ->
                viewModel.setQualityOption(option)
                dialog.dismiss()
            }
            .setSingleChoiceItems(optionsList, option) { dialog, which ->
                option = which
            }
            .show()
    }

    private fun setPlayer(player: ExoPlayer) {
        exoPlayerView = requireActivity().findViewById(R.id.stream_player_view)
        exoPlayerView.player = player
    }

    private fun startStream() {
        viewModel.start(
            onServiceBind = { broadcasterLogin },
            onPlayerCreated = { exoPlayer ->
                requireActivity().runOnUiThread {
                    setPlayer(exoPlayer)
                }
            }
        )
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

}