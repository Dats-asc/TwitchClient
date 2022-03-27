package com.example.twitchclient.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ActivityMainBinding
import com.example.twitchclient.ui.auth.AuthorizationActivity
import com.example.twitchclient.ui.games.GamesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentContainer: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentContainer = binding.navHostFragmentContainer

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_games -> {
                    true
                }
                R.id.navigation_popular -> {
                    true
                }
                R.id.navigation_followings -> {
                    true
                }
                else -> false
            }
        }
    }
}