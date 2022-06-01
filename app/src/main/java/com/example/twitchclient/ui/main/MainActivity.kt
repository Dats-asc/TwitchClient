package com.example.twitchclient.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.twitchclient.C
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ActivityMainBinding
import com.example.twitchclient.ui.followings.FollowingsFragment
import com.example.twitchclient.ui.navigation.NavOption
import com.example.twitchclient.ui.navigation.Navigator
import com.example.twitchclient.ui.search.SearchFragment
import com.example.twitchclient.utils.MyViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val destinationListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.streamFragment -> hideBottomNav()
                R.id.authFragment -> hideBottomNav()
                R.id.videoFragment -> hideBottomNav()
                R.id.startFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }

    @Inject
    lateinit var factory: MyViewModelFactory

    private val viewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setupNavigation()
    }

    override fun onBackPressed() {
        findNavController(R.id.nav_host_fragment_container).navigateUp()
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun setupNavigation() {
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment_container))
        findNavController(R.id.nav_host_fragment_container).addOnDestinationChangedListener(
            destinationListener
        )
    }
}