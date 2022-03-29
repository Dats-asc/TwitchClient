package com.example.twitchclient.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ActivityMainBinding
import com.example.twitchclient.ui.auth.AuthFragment
import com.example.twitchclient.ui.auth.AuthorizationActivity
import com.example.twitchclient.ui.followings.FollowingsFragment
import com.example.twitchclient.ui.games.GamesFragment
import com.example.twitchclient.ui.popular.PopularFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

const val TAG_GAMES = "TAG_GAME"
const val TAG_POPULAR = "TAG_POPULAR"
const val TAG_FOLLOWINGS = "TAG_FOLLOWINGS"
const val TAG_AUTH = "TAG_AUTH"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun init() {
        setupToolbar()
        setupBottomNavigation()
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        with(binding.toolbar){
            setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.action_search ->{
                        true
                    }

                    else -> false
                }
            }

            setNavigationOnClickListener {
                if(supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) is AuthFragment) {
                    supportFragmentManager.popBackStack()
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.setDisplayShowHomeEnabled(false)
                }
            }
        }
    }

    fun onAuthFragmentOpen(){
        supportFragmentManager.beginTransaction().run {
            replace(R.id.nav_host_fragment_container, AuthFragment())
            addToBackStack(TAG_AUTH)
            commit()
        }
        showBackArrow()
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBackArrow(){
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun hideBackArrow(){
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupBottomNavigation() {
        supportFragmentManager.beginTransaction().run {
            add(R.id.nav_host_fragment_container, FollowingsFragment(), TAG_FOLLOWINGS)
            addToBackStack(TAG_FOLLOWINGS)
            addToBackStack(TAG_FOLLOWINGS)
            commit()
        }
        binding.bottomNavigationView.selectedItemId = R.id.navigation_followings

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
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

    private fun itemGamesSelected() {
        replaceFragment(GamesFragment())
        supportActionBar?.title = getString(R.string.title_games)
    }

    private fun itemPopularSelected() {
        replaceFragment(PopularFragment())
        supportActionBar?.title = getString(R.string.title_popular)
    }

    private fun itemFollowingsSelected() {
        replaceFragment(FollowingsFragment())
        supportActionBar?.title = getString(R.string.title_followings)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTag = when (fragment) {
            is GamesFragment -> TAG_GAMES
            is PopularFragment -> TAG_POPULAR
            is FollowingsFragment -> TAG_FOLLOWINGS
            else -> ""
        }
        supportFragmentManager.beginTransaction().run {
            replace(
                R.id.nav_host_fragment_container,
                fragment,
                fragmentTag
                )
            commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.popBackStack()
    }
}