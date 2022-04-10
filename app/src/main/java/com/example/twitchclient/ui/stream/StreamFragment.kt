package com.example.twitchclient.ui.stream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.twitchclient.R
import com.google.android.exoplayer2.ui.PlayerView

class StreamFragment : Fragment() {

    private val playerView = activity?.findViewById<PlayerView>(R.id.stream_player_view)

    private lateinit var viewModel: StreamViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stream_fragment, container, false)
    }

}