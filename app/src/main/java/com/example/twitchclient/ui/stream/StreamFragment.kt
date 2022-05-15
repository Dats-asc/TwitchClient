package com.example.twitchclient.ui.stream

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
        it.root
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

    override fun onResume() {
        super.onResume()
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
            // Single-choice items (initialized with checked item)
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
        viewModel?.start(
            onServiceBind = { broadcasterLogin },
            onPlayerCreated = { exoPlayer ->
                requireActivity().runOnUiThread {
                    setPlayer(exoPlayer)
                }
            }
        )
    }

}