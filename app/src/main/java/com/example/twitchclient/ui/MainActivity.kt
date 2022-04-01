package com.example.twitchclient.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.twitchclient.R
import com.example.twitchclient.databinding.ActivityMainBinding
import com.example.twitchclient.ui.auth.AuthFragment
import com.example.twitchclient.ui.followings.FollowingsFragment
import com.example.twitchclient.ui.games.GamesFragment
import com.example.twitchclient.ui.navigation.Navigator
import com.example.twitchclient.ui.popular.PopularFragment

class MainActivity : AppCompatActivity(), Navigator {

    companion object {
        private const val USER_PREFERENCES = "USER_PREFERENCES"
        private const val USER_ACCESS_TOKEN_VALUE = "USER_ACCESS_TOKEN_VALUE"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferences: SharedPreferences

    private var currentNavigationItem = R.id.navigation_followings

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateToolbar(f)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun createFile() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun init() {
        preferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
        setupToolbar()
        setupBottomNavigation()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        with(binding.toolbar) {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_search -> {
                        backToStart()
                        true
                    }

                    else -> false
                }
            }

            setNavigationOnClickListener {
                if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) is AuthFragment) {
                    onBackPressed()
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    supportActionBar?.setDisplayShowHomeEnabled(false)
                    //binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
        supportFragmentManager.beginTransaction().run {
            add(R.id.nav_host_fragment_container, FollowingsFragment())
            commit()
        }

        binding.bottomNavigationView.selectedItemId = R.id.navigation_followings

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId != currentNavigationItem) {
                currentNavigationItem = item.itemId
                onNavigationItemSelected(currentNavigationItem)
                true
            } else
                false
        }
    }

    private fun onNavigationItemSelected(itemId: Int) {
        when (itemId) {
            R.id.navigation_games -> {
                replaceFragment(GamesFragment())
            }
            R.id.navigation_popular -> {
                replaceFragment(PopularFragment())
            }
            R.id.navigation_followings -> {
                replaceFragment(FollowingsFragment())
            }
        }
    }

    private fun updateToolbar(fragment: Fragment) {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        if (fragment is AuthFragment) {
            binding.bottomNavigationView.visibility = View.GONE
        } else {
            binding.bottomNavigationView.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            addToBackStack(null)
            replace(R.id.nav_host_fragment_container, fragment)
            commit()
        }
    }

    override fun backToStart() {
        with(supportFragmentManager) {
            popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            beginTransaction().run {
                replace(R.id.nav_host_fragment_container, FollowingsFragment())
                commit()
            }
        }
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.nav_host_fragment_container, fragment)
            commit()
        }
    }

    fun putAccessToken(token: String) {
        preferences.edit()
            .putString(USER_ACCESS_TOKEN_VALUE, token)
            .apply()
    }

    fun getAccessToken(): String? = preferences.getString(USER_ACCESS_TOKEN_VALUE, "")
}