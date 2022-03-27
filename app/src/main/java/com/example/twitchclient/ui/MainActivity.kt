package com.example.twitchclient.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ActivityMainBinding
import com.example.twitchclient.ui.auth.AuthorizationActivity
import com.example.twitchclient.ui.followings.FollowingsFragment
import com.example.twitchclient.ui.games.GamesFragment
import com.example.twitchclient.ui.popular.PopularFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentContainer: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        setupBottomNavigation()
    }

    private fun setupBottomNavigation(){
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_games -> {
                    itemGamesSelected()
                    true
                }
                R.id.navigation_popular -> {
                    itemPopularSelected()
                    true
                }
                R.id.navigation_followings -> {
                    itemFollowingsSelected()
                    true
                }
                else -> false
            }
        }
    }

    private fun itemGamesSelected(){
        replaceFragment(GamesFragment())
    }

    private fun itemPopularSelected(){
        replaceFragment(PopularFragment())
    }

    private fun itemFollowingsSelected(){
        replaceFragment(FollowingsFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().run {
            replace(R.id.nav_host_fragment_container, fragment)
            commit()
        }
    }
}