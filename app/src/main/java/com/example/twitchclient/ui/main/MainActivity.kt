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

    companion object {
        private const val USER_PREFERENCES = "USER_PREFERENCES"
        private const val USER_ACCESS_TOKEN_VALUE = "USER_ACCESS_TOKEN_VALUE"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferences: SharedPreferences

    private var navOption = NavOption.OPTION_DEFAULT

    private val destinationListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.streamFragment -> {
                    hideBottomNav()
                }
                else -> showBottomNav()
            }
        }

    @Inject
    lateinit var factory: MyViewModelFactory

    private val viewModel: MainViewModel by viewModels { factory }

    private var currentNavigationItem = R.id.navigation_followings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        putAccessToken("tthzw65o8p57eb80y0jwkr6lfcg3rt")
    }

    override fun onStart() {
        super.onStart()
        setupNavigation()
    }

    override fun onBackPressed() {
        findNavController(R.id.nav_host_fragment_container).navigateUp()
    }

    private fun init() {
        preferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun hideBottomNav(){
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNav(){
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.toolbar)
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment_container))
        findNavController(R.id.nav_host_fragment_container).addOnDestinationChangedListener(destinationListener)
    }

    fun putAccessToken(token: String) {
        preferences.edit()
            .putString(USER_ACCESS_TOKEN_VALUE, token)
            .apply()
    }

    fun getAccessToken(): String? = preferences.getString(USER_ACCESS_TOKEN_VALUE, null)
}